package ns.customFileFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import ns.exceptions.CorruptException;
import ns.exceptions.LoadingException;
import ns.openglObjects.Texture;
import ns.parallelComputing.TextureCreateRequest;
import ns.utils.GU;
import res.Resource;

public class TexFile implements File {
	private String location;

	public TexFile(String location) {
		this.location = location;
	}

	@Override
	public Texture load() throws LoadingException {
		Resource resource = new Resource().withLocation(location).withVersion(true).create();
		BufferedReader reader = GU.open(resource);
		int id = 0;
		int width = 0;
		int height = 0;
		try {
			String line = reader.readLine();
			String[] pts = line.split(" ");
			width = Integer.parseInt(pts[0]);
			height = Integer.parseInt(pts[1]);
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			if (resource.version() == 1) {
				for (int y = height - 1; y >= 0; y--) {
					line = reader.readLine();
					pts = line.split(" ");
					for (int x = 0; x < width; x++) {
						try {
							int pixel = Integer.parseInt(pts[x]);
							pixels.put((byte) ((pixel >> 16) & 0xFF));
							pixels.put((byte) ((pixel >> 8) & 0xFF));
							pixels.put((byte) (pixel & 0xFF));
							pixels.put((byte) ((pixel >> 24) & 0xFF));
						} catch (NumberFormatException e) {
							throw new CorruptException("File at " + location + " is corrupt(found: \"" + pts[x]
									+ "\" while expecting an int)");
						}
					}
				}
			} else if (resource.version() == 2) {
				InputStream is = resource.asInputStream();
				for (int i = 0; i < width * height * 4; i++) {
					int val = is.read();
					pixels.put((byte) val);
					System.out.println((char) val);
				}
			}
			pixels.flip();

			if (Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
				id = GL11.glGenTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA,
						GL11.GL_UNSIGNED_BYTE, pixels);

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			} else {
				Texture target = new Texture(0, width, height);
				GU.sendRequestToMainThread(new TextureCreateRequest(target, pixels));
				return target;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (id == 0 ? null : new Texture(id, width, height));
	}
}