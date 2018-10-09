package ns.converting;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;

class PngToTexConverter {
	public static void main(String[] args) throws IOException {
		byte[] buf = new byte[50];
		int len = System.in.read(buf);
		String location = "";
		for (int i = 0; i < len - 1; i++)
			location += (char) buf[i];
//		if (location.equals("UPDATE ALL")) {
//			write(new File("gameData"));
//		} else {
		File target = new File("../gameData/textures/" +
				location);
		write(target);
//		}
	}

	private static void write(File f) throws IOException {
		if (f.isDirectory()) {
			for (File fl : Objects.requireNonNull(f.listFiles()))
				if (fl.getName().endsWith(".png") || fl.isDirectory()) {
					write(fl);
				}
		} else {
			File target = new File(f.getPath().replace(".png", ".tex"));
			target.createNewFile();
			OutputStream outStr = new FileOutputStream(target);
			BufferedImage img = ImageIO.read(new FileInputStream(f));
			int width = img.getWidth();
			int height = img.getHeight();
			int version = 4;
			outStr.write(version);
			outStr.write(getBytes(width));
			outStr.write(getBytes(height));
			for (int y = height - 1; y >= 0; y--) {
				for (int x = 0; x < width; x++) {
					int pixel = img.getRGB(x, y);
					outStr.write((pixel >> 16) & 0xFF);
					outStr.write((pixel >> 8) & 0xFF);
					outStr.write(pixel & 0xFF);
					outStr.write((pixel >> 24) & 0xFF);
				}
			}
			outStr.close();
		}
	}

	private static final ByteBuffer buffer = ByteBuffer.allocate(4);

	private static byte[] getBytes(int i) {
		buffer.clear();
		buffer.putInt(i);
		return buffer.array();
	}
}