package ns.utils;

import data.GameData;
import data.SaveData;
import ns.components.Blueprint;
import ns.components.BlueprintCreator;
import ns.display.DisplayManager;
import ns.exceptions.GameException;
import ns.fontMeshCreator.FontType;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import ns.parallelComputing.CreateVAORequest;
import ns.parallelComputing.Request;
import ns.parallelComputing.Thread;
import ns.parallelComputing.ThreadMaster;
import ns.renderers.MasterRenderer;
import ns.time.DayNightCycle;
import ns.time.Time;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import resources.In;
import resources.Out;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GU {
	public static final Random random = new Random();
	public static boolean prevFrameClicked;
	public static Vector3f mouseDelta;
	public static float lastFramesLengths;
	private static final float[] mouseLengths = new float[20];
	private static final List<Texture> textures = new ArrayList<>();
	public static final int CURRENT_WORLD_FILE_VERSION = 4;
	public static final int TOTAL_NUMBER_OF_ENTITIES = 13;
	private static final List<IntBuffer> textureBuffers = new ArrayList<>();
	public static final String WORLD_SAVE_FILE_FORMAT = "nssv";
	public static final String MAIN_THREAD_NAME = "main thread";
	public static FontType Z003;
	public static FontType caladea;
	public static String path;
	private static final ByteBuffer buffer = ByteBuffer.allocateDirect(4);
	public static Vector2b mouseButtons;
	private static final DocumentBuilder documentBuilder;
	public static final boolean OS_WINDOWS;
	public static final boolean OS_LINUX;
	public static Time time;

	static {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setValidating(true);
		try {
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new GameException(e.getMessage(), e.getStackTrace());
		}
		String os = System.getProperty("os.name");
		OS_WINDOWS = os.equals("Windows");
		OS_LINUX = os.equals("Linux");
		time = new Time(new DayNightCycle() {
			@Override
			public boolean isDay(float t) {
				float h = (t / H_S_DURATION) % 24f;
				return h >= 8f && h < 17f;
			}

			@Override
			public boolean isNight(float t) {
				float h = (t / H_S_DURATION) % 24f;
				return (h >= 21f && h < 24f) || (h >= 0f && h < 5f);
			}

			@Override
			public boolean isMorning(float t) {
				float h = (t / H_S_DURATION) % 24f;
				return h >= 5f && h < 8f;
			}

			@Override
			public boolean isEvening(float t) {
				float h = (t / H_S_DURATION) % 24f;
				return h >= 17f && h < 21f;
			}

			@Override
			public float dayFactor(float t) {
				if (isDay(t))
					return 1;
				else if (isEvening(t)) {
					return 1.0f - (t / H_S_DURATION % 24f - 17f) / 4f;
				} else if (isMorning(t)) {
					return (t / H_S_DURATION % 24f - 5f) / 3f;
				} else return 0f;
			}

			@Override
			public float nightFactor(float t) {
				if (isNight(t))
					return 1;
				else if (isEvening(t)) {
					return ((t / H_S_DURATION) % 24f - 17f) / 4f;
				} else if (isMorning(t)) {
					return 1.0f - ((t / H_S_DURATION) % 24f - 5f) / 3f;
				} else return 0f;
			}
		});
	}

	public static void init() {
		GameData.init();
		SaveData.init();
	}

	public static BufferedReader open(In resource) {
		return new BufferedReader(new InputStreamReader(resource.asInputStream()));
	}

	public static PrintWriter open(Out resource) {
		return new PrintWriter(resource.asOutputStream());
	}

	public static void rn_update() {
		mouseDelta = new Vector3f(Mouse.getDX(), Mouse.getDY(), Mouse.getDWheel());
		mouseButtons = new Vector2b(Mouse.isButtonDown(0), Mouse.isButtonDown(1));
		time.update();
	}

	public static void update() {
		prevFrameClicked = Mouse.isButtonDown(0);
		for (Key k : Key.values())
			k.setKeyPressedPrevFrame(k.isPressed());
		for (int i = 0; i < mouseLengths.length - 1; i++)
			mouseLengths[i] = mouseLengths[i + 1];
		mouseLengths[mouseLengths.length - 1] = mouseDelta.length();
		lastFramesLengths = 0;
		for (float l : mouseLengths)
			lastFramesLengths += l;
	}

	public static void updateWireFrame() {
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, (Key.KEY_W.isPressed() ? GL11.GL_LINE : GL11.GL_FILL)); // Press
		// key 'W'
		// on
		// the
		// keyboard
		// for
		// wire-frame
	}

	public static void sendRequestToMainThread(Request r) {
		Thread th = ThreadMaster.getThread(MAIN_THREAD_NAME);
		while (th.isExecutingRequests)
			java.lang.Thread.yield();
		th.setToCarryOutRequest(r);
	}

	public static ns.parallelComputing.Thread currentThread() {
		return (Thread) java.lang.Thread.currentThread();
	}

	public static void setMouseCursor(Cursor cursor) {
		try {
			Mouse.setNativeCursor(cursor);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void initMouseCursors(MasterRenderer renderer) {
		FBO fbo = new FBO(64, 64, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		fbo.bind();
		GL11.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		for (int i = 0; i < TOTAL_NUMBER_OF_ENTITIES; i++) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			Blueprint blueprint = BlueprintCreator.createModelBlueprintFor(Integer.toString(1000 + i))
					.withDefaultCustomColors();
			boolean shouldScale = blueprint.getModel().shouldScale();
			renderer.render(blueprint, new Vector3f(0f, shouldScale ? -1f : -3f, shouldScale ? -6.6f : -20f));
			textures.add(i, fbo.getTex());
			textureBuffers.add(fbo.getTex().getAsIntBuffer());
			fbo.createNewTexture();
		}
		fbo.setTextureNull();
		FBO.unbind();
		fbo.delete();
	}

	public static String getGL20Type(int type) {
		Field[] fields = GL20.class.getFields();
		for (Field field : fields) {
			if (field.getType() == int.class) {
				try {
					int i = (int) field.get(null);
					if (i == type) {
						return field.getName();
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static boolean hasVaoCreateRequestInMainThread(VAO vao) {
		for (CreateVAORequest req : currentThread().vaoCreateRequests)
			if (req.vao == vao) {
				return true;
			}
		return false;
	}

	public static class Random {
		private final java.util.Random random = new java.util.Random();

		public int genInt(int max) {
			return random.nextInt(max);
		}

		public float genFloat() {
			return random.nextFloat();
		}

		public void setSeed(long seed) {
			random.setSeed(seed);
		}

		public double genDouble() {
			return random.nextDouble();
		}
	}

	public enum Key {
		KEY_S(Keyboard.KEY_S), KEY_W(Keyboard.KEY_W), KEY_ESC(Keyboard.KEY_ESCAPE);

		private final int id;
		private boolean pressedPrevFrame;

		Key(int id) {
			this.id = id;
		}

		boolean isPressed() {
			return Keyboard.isKeyDown(id);
		}

		public boolean pressedPreviousFrame() {
			return pressedPrevFrame;
		}

		private void setKeyPressedPrevFrame(boolean pressed) {
			pressedPrevFrame = pressed;
		}

		public boolean pressedThisFrame() {
			return isPressed() && !pressedPrevFrame;
		}
	}

	public static Cursor createCursor(int xHotspot, int yHotspot, int nrOfFrames, IntBuffer textures,
	                                  IntBuffer delays) {
		try {
			return new Cursor(64, 64, xHotspot, yHotspot, nrOfFrames, textures, delays);
		} catch (LWJGLException e) {
			return null;
		}
	}

	public static IntBuffer getMouseTexture(String modelFolder, int sub) {
		int idx = Integer.parseInt(modelFolder) - 1000;
		return textures.get(idx).getAsIntBuffer(sub, textureBuffers.get(idx));
	}

	public static Cursor createCursor(int xHotspot, int yHotspot, int nrOfFrames, IntBuffer textures, IntBuffer delays,
	                                  int sub) {
		try {
			return new Cursor(64 - sub, 64 - sub, xHotspot, yHotspot - sub, nrOfFrames, textures, delays);
		} catch (LWJGLException e) {
			return null;
		}
	}

	public static int binaryInt(String string) {
		string = string.replaceAll(" ", "");
		return Integer.parseInt(string, 2);
	}

	public static int sizeof(int gl_type) {
		if (gl_type == GL11.GL_FLOAT)
			return 4;
		if (gl_type == GL11.GL_BYTE)
			return 1;
		if (gl_type == GL11.GL_INT)
			return 4;
		return 0;
	}

	public static FloatBuffer storeDataInFloatBuffer(float[] data) {
		return BufferUtils.createFloatBuffer(data.length).put(data).flip();
	}

	public static IntBuffer storeDataInIntBuffer(int[] data) {
		return BufferUtils.createIntBuffer(data.length).put(data).flip();
	}

	public static ByteBuffer storeDataInByteBuffer(byte[] data) {
		return BufferUtils.createByteBuffer(data.length).put(data).flip();
	}

	public static Vector2f normalizedMousePos() {
		double normalizedX = -1.0 + 2.0 * (double) Mouse.getX() / Display.getWidth();
		double normalizedY = -(1.0 - 2.0 * (double) Mouse.getY() / Display.getHeight());
		return new Vector2f((float) normalizedX, (float) normalizedY);
	}

	public static void setZ003(FontType font) {
		Z003 = font;
	}

	public static float clamp(float val, float min, float max) {
		if (min > max)
			return clamp(val, max, min);

		if (val < min)
			return min;
		else if (val > max)
			return max;
		return val;
	}

	public static void setCaladea(FontType caladea) {
		GU.caladea = caladea;
	}

	public static Matrix4f creteProjection(float FOV, float NEAR_PLANE, float FAR_PLANE) {
		Matrix4f projectionMatrix = new Matrix4f();
		float aspectRatio = (float) DisplayManager.WIDTH / (float) DisplayManager.HEIGHT;

		float y_scale = 1f / (float) Math.tan(FOV / 2f);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;

		return projectionMatrix;
	}

	public static String getGLErrorType(int err) {
		Field[] fields = GL11.class.getFields();
		for (Field field : fields) {
			if (field.getType() == int.class) {
				try {
					int i = (int) field.get(null);
					if (i == err)
						return field.getName();
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static float readFloat(byte b1, byte b2, byte b3, byte b4) {
		return ByteBuffer.wrap(new byte[]{b1, b2, b3, b4}).order(ByteOrder.BIG_ENDIAN).getFloat();
	}

	public static float readFloat(int i) {
		return ByteBuffer.wrap(getBytes(i)).order(ByteOrder.BIG_ENDIAN).getFloat();
	}

	public static int readInt(byte b1, byte b2, byte b3, byte b4) {
		return ByteBuffer.wrap(new byte[]{b1, b2, b3, b4}).order(ByteOrder.BIG_ENDIAN).getInt();
	}

	public static byte[] getBytes(float f) {
		buffer.clear();
		buffer.putFloat(f);
		return buffer.array();
	}

	public static byte[] getBytes(int i) {
		buffer.clear();
		buffer.putInt(i);
		return buffer.array();
	}

	public static Document getDocument(InputStream in) {
		try {
			return documentBuilder.parse(in);
		} catch (IOException | SAXException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isStringOfPattern(String string, String pattern) {
		return Pattern.compile(pattern).matcher(string).matches();
	}

	public static Vector3f mix(Vector3f a, Vector3f b, float blend) {
		return new Vector3f(a.x * (1.0f - blend) + b.x * blend, a.y * (1.0f - blend) + b.y * blend,
				a.z * (1.0f - blend) + b.z * blend);
	}
}