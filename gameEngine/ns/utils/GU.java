package ns.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.components.BlueprintCreator;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.parallelComputing.Request;
import ns.parallelComputing.ThreadMaster;
import ns.renderers.MasterRenderer;
import res.Resource;
import res.WritingResource;

public class GU {
	public static final Random random = new Random();
	public static boolean prevFrameClicked;
	public static Vector2f mouseDelta;
	public static float lastFramesLengths;
	private static float[] mouseLengths = new float[20];
	public static final List<Texture> textures = new ArrayList<>();
	public static final int TOTAL_NUMBER_OF_ENTITIES = 5;
	public static final int CURRENT_WORLD_FILE_VERSION = 1;
	public static final String WORLD_SAVE_FILE_FORMAT = "nssv";

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
		ThreadMaster.getThread("main thread").setToCarryOutRequest(r);
	}
	
	public static ns.parallelComputing.Thread currentThread() {
		return (ns.parallelComputing.Thread) Thread.currentThread();
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
			renderer.render(
					BlueprintCreator.createModelBlueprintFor(Integer.toString(1000 + i)).withDefaultCustomColors(),
					new Vector3f(0f, -3f, -20f));
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
		KEY_S(Keyboard.KEY_S), KEY_W(Keyboard.KEY_W);

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
}