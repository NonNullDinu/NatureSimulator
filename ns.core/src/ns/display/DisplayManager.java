package ns.display;

import data.GameData;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

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
			BufferedImage icon = ImageIO.read(GameData.getResourceAt("textures/ns_icon.png").asInputStream());
			Display.setTitle("Nature Simulator");
			Display.setIcon(new ByteBuffer[]{loadIconInstance(icon, 128), loadIconInstance(icon, 32),
					loadIconInstance(icon, 16)});
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(), new ContextAttribs(4, 3).withForwardCompatible(true).withProfileCore(true));
			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException | IOException e) {
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

	public static void setWindowVisible() {
//		window.setSize(WIDTH, HEIGHT);
//		window.setVisible(true);
	}

	private static ByteBuffer loadIconInstance(BufferedImage image, int dimension) {
		BufferedImage scaledIcon = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = scaledIcon.createGraphics();
		double ratio = 1;
		if (image.getWidth() > scaledIcon.getWidth()) {
			ratio = (double) (scaledIcon.getWidth()) / image.getWidth();
		} else {
			ratio = scaledIcon.getWidth() / image.getWidth();
		}
		if (image.getHeight() > scaledIcon.getHeight()) {
			double r2 = (double) (scaledIcon.getHeight()) / image.getHeight();
			if (r2 < ratio) {
				ratio = r2;
			}
		} else {
			double r2 = (scaledIcon.getHeight() / image.getHeight());
			if (r2 < ratio) {
				ratio = r2;
			}
		}
		double width = image.getWidth() * ratio;
		double height = image.getHeight() * ratio;
		g.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2), (int) ((scaledIcon.getHeight() - height) / 2),
				(int) (width), (int) (height), null);
		g.dispose();

		byte[] imageBuffer = new byte[dimension * dimension * 4];
		int counter = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				int colorSpace = scaledIcon.getRGB(j, i);
				imageBuffer[counter + 0] = (byte) ((colorSpace << 8) >> 24);
				imageBuffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
				imageBuffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
				imageBuffer[counter + 3] = (byte) (colorSpace >> 24);
				counter += 4;
			}
		}
		return ByteBuffer.wrap(imageBuffer);
	}
}