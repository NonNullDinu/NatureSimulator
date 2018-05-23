package ns.openglObjects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import res.Resource;

public class Texture implements IOpenGLObject {
	private static List<Texture> textures = new ArrayList<>();

	private Resource resource;
	private int id;

	public Texture(String location) {
		this(new Resource(location));
	}

	public Texture(Resource resource) {
		this.resource = resource;
	}

	public Texture(int id) {
		this.id = id;
	}

	public Texture create() {
		BufferedImage asImage = null;
		if (resource.exists()) {
			try {
				asImage = ImageIO.read(resource.asInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			int width = asImage.getWidth();
			int height = asImage.getHeight();
			int[] pixels_raw = asImage.getRGB(0, 0, width, height, null, 0, height);
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			for (int y = height - 1; y >= 0; y--)
				for (int x = 0; x < width; x++) {
					int pixel = pixels_raw[y * width + x];
					pixels.put((byte) ((pixel >> 16) & 0xFF));
					pixels.put((byte) ((pixel >> 8) & 0xFF));
					pixels.put((byte) (pixel & 0xFF));
					pixels.put((byte) ((pixel >> 24) & 0xFF));
				}
			pixels.flip();

			id = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, pixels);
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

			return this;
		} else
			return null;
	}

	public void bindToTextureUnit(int texUnit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + texUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	@Override
	public void delete() {
		GL11.glDeleteTextures(id);
	}

	@Override
	public int getID() {
		return id;
	}
	
	public static void cleanUp() {
		for(Texture tex : textures)
			tex.delete();
	}
}
