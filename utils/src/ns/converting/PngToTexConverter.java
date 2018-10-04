package ns.converting;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PngToTexConverter {
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
			for (File fl : f.listFiles())
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
			if (version == 1) {
				PrintWriter writer = new PrintWriter(outStr);
				writer.write(width + " " + height + "\n");
				for (int y = height - 1; y >= 0; y--) {
					for (int x = 0; x < width; x++) {
						writer.write((x == 0 ? "" : " ") + img.getRGB(x, y));
					}
					writer.write((int) '\n');
				}
				writer.close();
			} else if (version == 2) {
				outStr.write((width + " " + height + "~").getBytes());
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
			} else if (version == 3) {
				List<Integer> intData = new ArrayList<>();
				List<Byte> data = new ArrayList<>();
				for (int y = height - 1; y >= 0; y--) {
					for (int x = 0; x < width; x++) {
						int px = img.getRGB(x, y);
						intData.add(px);
						data.add((byte) ((px >> 16) & 0xFF));
						data.add((byte) ((px >> 8) & 0xFF));
						data.add((byte) (px & 0xFF));
						data.add((byte) ((px >> 24) & 0xFF));
					}
				}
				byte mb = 0;
				boolean use = true;
				while (data.contains(mb)) {
					mb++;
					if (mb == 0) {
						use = false;
						break;
					}
				}
				Byte[] mbArray = null;
				if (use) {
					outStr.write(mb);
				} else {
					outStr.write(-1);
					mbArray = new Byte[]{0, 1};
				}
				outStr.write(getBytes(width));
				outStr.write(getBytes(height));
				List<Byte> compressedData = new ArrayList<>();
				for (int i = 0; i < intData.size(); ) {
					int cdata = intData.get(i);
					int startI = i;
					i++;
					for (; i < intData.size() && cdata == intData.get(i) && i - startI < 256; i++) {
					}
					compressedData.add((byte) ((cdata >> 16) & 0xFF));
					compressedData.add((byte) ((cdata >> 8) & 0xFF));
					compressedData.add((byte) (cdata & 0xFF));
					compressedData.add((byte) ((cdata >> 24) & 0xFF));
					if (startI < i - 1) {
						if (use)
							compressedData.add(mb);
						else {
							compressedData.add(mbArray[0]);
							compressedData.add(mbArray[1]);
						}
						compressedData.add((byte) (i - startI));
					}
				}
				for (byte b : compressedData)
					outStr.write(b);
				outStr.close();
			} else if (version == 4) {
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
	}

	private static final ByteBuffer buffer = ByteBuffer.allocate(4);

	private static byte[] getBytes(int i) {
		buffer.clear();
		buffer.putInt(i);
		byte[] bytes = buffer.array();
		return bytes;
	}
}