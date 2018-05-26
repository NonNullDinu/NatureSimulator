package ns.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.lwjgl.input.Mouse;

import res.Resource;
import res.WritingResource;

public class GU {
	public static final Random random = new Random();
	public static boolean prevFrameClicked;

	public static BufferedReader open(Resource resource) {
		return new BufferedReader(new InputStreamReader(resource.asInputStream()));
	}

	public static PrintWriter open(WritingResource resource) {
		return new PrintWriter(resource.asOutputStream());
	}
	
	public static void update() {
		prevFrameClicked = Mouse.isButtonDown(0);
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
}