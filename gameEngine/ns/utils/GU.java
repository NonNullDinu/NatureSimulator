package ns.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import res.Resource;
import res.WritingResource;

public class GU {
	public static final Random random = new Random();
	public static boolean prevFrameClicked;
	public static Vector2f mouseDelta;
	public static float lastFramesLengths;
	private static float[] mouseLengths = new float[10];

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
		for(int i = 0; i < mouseLengths.length - 1; i++)
			mouseLengths[i] = mouseLengths[i + 1];
		mouseLengths[mouseLengths.length - 1] = mouseDelta.length();
		lastFramesLengths = 0;
		for(float l : mouseLengths)
			lastFramesLengths += l;
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
		KEY_S(Keyboard.KEY_S);

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
}