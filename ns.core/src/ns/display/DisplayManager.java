package ns.display;

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
//	private static JFrame window;
//	private static Canvas canvas;
//	private static boolean closeRequested;

	public static void createDisplay() {
		try {
//			window = new JFrame();
//			window.addWindowListener(new WindowListener() {
//				@Override
//				public void windowOpened(WindowEvent e) {}
//
//				@Override
//				public void windowClosing(WindowEvent e) {
//					closeRequested = true;
//				}
//
//				@Override
//				public void windowClosed(WindowEvent e) {
//					closeRequested = true;
//				}
//
//				@Override
//				public void windowIconified(WindowEvent e) {}
//
//				@Override
//				public void windowDeiconified(WindowEvent e) {}
//
//				@Override
//				public void windowActivated(WindowEvent e) {
//					System.out.println("Activated " + System.nanoTime());
//				}
//
//				@Override
//				public void windowDeactivated(WindowEvent e) {
//					System.out.println("Deactivated " + System.nanoTime());
//				}
//			});
//			window.setSize(0, 0);
//			window.setLocationRelativeTo(null);
//			try {
//				window.setIconImage(ImageIO.read(GameData.getResourceAt("textures/ns_icon.png").asInputStream()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			canvas = new Canvas();
//			window.getContentPane().add(canvas);
//			window.setVisible(true);
//			window.setResizable(false);
//			window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//			Display.setParent(canvas);
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(), new ContextAttribs(4, 3).withForwardCompatible(true).withProfileCore(true));
			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		lastFrameTime = getCurrentTime();
	}

	public static void updateDisplay() {
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
//		window.dispose();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	public static void setWindowVisible(){
//		window.setSize(WIDTH, HEIGHT);
//		window.setVisible(true);
	}
}