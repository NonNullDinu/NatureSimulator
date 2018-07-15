package ns.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import data.GameData;
import data.SaveData;
import ns.components.Blueprint;
import ns.components.BlueprintCreator;
import ns.fontMeshCreator.FontType;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.parallelComputing.Request;
import ns.parallelComputing.Thread;
import ns.parallelComputing.ThreadMaster;
import ns.renderers.MasterRenderer;
import resources.Resource;
import resources.WritingResource;

public class GU {
	public static final Random random = new Random();
	public static boolean prevFrameClicked;
	public static Vector2f mouseDelta;
	public static float lastFramesLengths;
	private static float[] mouseLengths = new float[20];
	public static final List<Texture> textures = new ArrayList<>();
	public static final int TOTAL_NUMBER_OF_ENTITIES = 13;
	public static final int CURRENT_WORLD_FILE_VERSION = 3;
	public static final String WORLD_SAVE_FILE_FORMAT = "nssv";
	public static final String MAIN_THREAD_NAME = "main thread";
	public static final IntBuffer tempibuffer = BufferUtils.createIntBuffer(1);
	public static FontType Z003;
	public static FontType caladea;
	public static String path;
	private static ByteBuffer buffer = ByteBuffer.allocate(4);
	
	public static void init() {
		GameData.init();
		SaveData.init();
	}

	public static BufferedReader open(Resource resource) {
		return new BufferedReader(new InputStreamReader(resource.asInputStream()));
	}

	public static PrintWriter open(WritingResource resource) {
		return new PrintWriter(resource.asOutputStream());
	}

	public static void update() {
		prevFrameClicked = Mouse.isButtonDown(0);
		mouseDelta = new Vector2f(Mouse.getDX(), Mouse.getDY());
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
			Thread.yield();
		th.setToCarryOutRequest(r);
	}

	public static Thread currentThread() {
		return (Thread) Thread.currentThread();
	}

	public static void setMouseCursor(Cursor cursor) {
		try {
			Mouse.setNativeCursor(cursor);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static final synchronized void initMouseCursors(MasterRenderer renderer) {
		FBO fbo = new FBO(64, 64, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		fbo.bind();
		GL11.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		for (int i = 0; i < TOTAL_NUMBER_OF_ENTITIES; i++) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			Blueprint blueprint = BlueprintCreator.createModelBlueprintFor(Integer.toString(1000 + i)).withDefaultCustomColors();
			boolean shouldScale = blueprint.getModel().shouldScale();
			renderer.render(
					blueprint,
					new Vector3f(0f, shouldScale ? -1f : -3f, shouldScale ? -6.6f : -20f));
			textures.add(i, fbo.getTex());
			fbo.createNewTexture();
		}
		fbo.setTextureNull();
		FBO.unbind();
		fbo.delete();
	}

	public static class Random {
		private final java.util.Random random = new java.util.Random();

		public int genInt(int max) {
			return random.nextInt(max);
		}

		public float genFloat() {
			return random.nextFloat();
		}
	}

	public enum Key {
		KEY_S(Keyboard.KEY_S), KEY_W(Keyboard.KEY_W), KEY_ESC(Keyboard.KEY_ESCAPE);

		private int id;
		private boolean pressedPrevFrame;

		private Key(int id) {
			this.id = id;
		}

		public boolean isPressed() {
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
			e.printStackTrace();
			return null;
		}
	}

	public static IntBuffer getMouseTexture(String modelFolder, int sub) {
		int idx = Integer.parseInt(modelFolder) - 1000;
		return textures.get(idx).getAsIntBuffer(sub);
	}

	public static Cursor createCursor(int xHotspot, int yHotspot, int nrOfFrames, IntBuffer textures, IntBuffer delays,
			int sub) {
		try {
			return new Cursor(64 - sub, 64 - sub, xHotspot, yHotspot - sub, nrOfFrames, textures, delays);
		} catch (LWJGLException e) {
			e.printStackTrace();
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
		return (FloatBuffer) BufferUtils.createFloatBuffer(data.length).put(data).flip();
	}

	public static IntBuffer storeDataInIntBuffer(int[] data) {
		return (IntBuffer) BufferUtils.createIntBuffer(data.length).put(data).flip();
	}

	public static ByteBuffer storeDataInByteBuffer(byte[] data) {
		return (ByteBuffer) BufferUtils.createByteBuffer(data.length).put(data).flip();
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
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();

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
					if(i == err)
						return field.getName();
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static float readFloat(byte b1, byte b2, byte b3, byte b4) {
		buffer.clear();
		buffer.put(b1);
		buffer.put(b2);
		buffer.put(b3);
		buffer.put(b4);
		buffer.flip();
		return buffer.getFloat();
	}

	public static int readInt(byte b1, byte b2, byte b3, byte b4) {
		buffer.clear();
		buffer.put(b1);
		buffer.put(b2);
		buffer.put(b3);
		buffer.put(b4);
		buffer.flip();
		return buffer.getInt();
	}

	public static byte[] getBytes(float f) {
		buffer.clear();
		buffer.putFloat(f);
		buffer.flip();
		byte[] bytes = new byte[] {
				buffer.get(), buffer.get(), buffer.get(), buffer.get()
		};
		return bytes;
	}
	
	public static byte[] getBytes(int i) {
		buffer.clear();
		buffer.putInt(i);
		buffer.flip();
		byte[] bytes = new byte[] {
				buffer.get(), buffer.get(), buffer.get(), buffer.get()
		};
		return bytes;
	}
}