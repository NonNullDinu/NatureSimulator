package ns.converting;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import res.Resource;

public class PngToTexConvertor {
	public static void main(String[] args) throws IOException {
		byte[] buf = new byte[50];
		int len = System.in.read(buf);
		String location = "";
		for (int i = 0; i < len - 1; i++)
			location += (char) buf[i];
		File target = new File("resources/" + location.replace(".png", ".tex"));
		System.out.println("resources/" + location.replace(".png", ".tex"));
		target.createNewFile();
		BufferedWriter writer = new BufferedWriter(
				new FileWriter(target));
		BufferedImage img = ImageIO.read(new Resource().withLocation(location).withVersion(false).create().asInputStream());
		int width = img.getWidth();
		int height = img.getHeight();
		writer.write(width + " " + height + "\n");
		for(int y = height - 1; y >= 0; y--) {
			for(int x = 0; x < width; x++) {
				writer.write((x == 0 ? "" : " ") + img.getRGB(x, y));
			}
			writer.write((int) '\n');
		}
		writer.close();
	}
}