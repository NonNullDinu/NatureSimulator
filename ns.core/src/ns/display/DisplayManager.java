package ns.display;

import ns.configuration.Config;
import ns.configuration.GameConfig;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static long lastFrameTime;
	private static float delta;
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 800;

	public static void createDisplay() {
		try {
			if (GameConfig.getConfig(GameConfig.FULLSCREEN) == Config.TRUE)
				Display.setFullscreen(true);
			else
				Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), new ContextAttribs(4, 3).withForwardCompatible(true).withProfileCore(true)
					.withProfileCompatibility(false));
			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		lastFrameTime = getCurrentTime();
	}

	public static void updateDisplay() {
		Display.sync(120);
		Display.update();
		long currentTime = getCurrentTime();
		delta = (currentTime - lastFrameTime) / 1000f;
		lastFrameTime = currentTime;
//		System.out.println(1f / delta); // Display FPS in console
	}

	public static void closeDisplay() {
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}
}