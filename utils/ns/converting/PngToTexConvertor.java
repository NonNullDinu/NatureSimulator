package ns.converting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
		PrintWriter writer = GU.open(new WritingResource(target.getPath()));
		BufferedImage img = ImageIO
				.read(new Resource().withLocation("textures/" + location).withVersion(false).create().asInputStream());
		int width = img.getWidth();
		int height = img.getHeight();
		int version = 1; // version 2 is deprecated 
		writer.write(version);
		writer.write(width + " " + height + "\n");
		if (version == 1) {
			for (int y = height - 1; y >= 0; y--) {
				for (int x = 0; x < width; x++) {
					writer.write((x == 0 ? "" : " ") + img.getRGB(x, y));
				}
				writer.write((int) '\n');
			}
		} else if (version == 2) {
			for (int y =height - 1; y >= 0; y--) {
				for (int x = 0; x < width; x++) {
					int pixel = img.getRGB(x, y);
					writer.write((pixel >> 16) & 0xFF);
					writer.write((pixel >> 8) & 0xFF);
					writer.write(pixel & 0xFF);
					writer.write((pixel >> 24) & 0xFF);
				}
			}
		}
		writer.close();
	}
}