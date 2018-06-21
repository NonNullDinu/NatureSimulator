package ns.converting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import ns.utils.GU;
import res.Resource;
import res.WritingResource;

public class PngToTexConvertor {
	public static void main(String[] args) throws IOException {
		byte[] buf = new byte[50];
		int len = System.in.read(buf);
		String location = "";
		for (int i = 0; i < len - 1; i++)
			location += (char) buf[i];
		File target = new File("resources/res/textures/" + location.replace(".png", ".tex"));
		target.createNewFile();
		WritingResource output = new WritingResource().withLocation(target.getPath()).create();
		OutputStream outStr = output.asOutputStream();
		BufferedImage img = ImageIO
				.read(new Resource().withLocation("textures/" + location).withVersion(false).create().asInputStream());
		int width = img.getWidth();
		int height = img.getHeight();
		int version = 2; // version 2 is functional (and recommended)
		output.writeVersion(version);
		if (version == 1) {
			PrintWriter writer = GU.open(output);
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
		}
	}
}