package ns.display;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import ns.configuration.Config;
import ns.configuration.GameConfig;

public class DisplayManager {

	private static long lastFrameTime;
	private static float delta;

	public static void createDisplay() {
		try {
			if (GameConfig.getConfig(GameConfig.FULLSCREEN) == Config.TRUE)
				Display.setFullscreen(true);
			else
				Display.setDisplayMode(new DisplayMode(1200, 800));
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(),
					new ContextAttribs(4, 3).withForwardCompatible(true).withProfileCore(true));
			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		lastFrameTime = getCurrentTime();
	}

	public static void updateDisplay() {
		Display.update();
		long currentTime = getCurrentTime();
		delta = (currentTime - lastFrameTime) / 1000f;
		lastFrameTime = currentTime;
	}

	public static void closeDisplay() {
		Display.destroy();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}
}