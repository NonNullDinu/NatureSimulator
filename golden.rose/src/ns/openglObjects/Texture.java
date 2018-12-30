/*
 * Copyright (C) 2018  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.openglObjects;

import ns.parallelComputing.TextureCreateRequest;
import ns.utils.GU;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Texture implements IOpenGLObject {
	private static final List<Texture> textures = new ArrayList<>();
	private final int width;
	private final int height;
	private int id;
	private boolean created = false;

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public Texture(int width, int height, byte pixel_red, byte pixel_green, byte pixel_blue, byte pixel_alpha) {
		this.width = width;
		this.height = height;
		ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
		byte[] pixel = new byte[]{pixel_red, pixel_green, pixel_blue, pixel_alpha};
		for (int i = 0; i < width * height; i++)
			buf.put(pixel);
		buf.flip();
		if (GU.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			this.id = GL11.glGenTextures();
			loadWith(buf);
		} else
			GU.sendRequestToMainThread(new TextureCreateRequest(this, buf));
	}

	public static void cleanUp() {
		for (Texture tex : textures)
			tex.delete();
	}

	public Texture create() {
		return this;
	}

	public void bindToTextureUnit(int texUnit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + texUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	@Override
	public void delete() {
		GL11.glDeleteTextures(id);
		created = false;
	}

	@Override
	public int getID() {
		return id;
	}

	public IntBuffer getAsIntBuffer() {
		IntBuffer pixels = BufferUtils.createIntBuffer(width * height);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return pixels;
	}

	public IntBuffer getAsIntBuffer(int sub, IntBuffer pixels) {
		IntBuffer true_pixels = BufferUtils.createIntBuffer((width - sub) * (height - sub));
		for (int y = sub / 2; y < height - sub / 2; y++) {
			for (int x = sub / 2; x < width - sub / 2; x++) {
				true_pixels.put(pixels.get(y * width + x));
			}
		}
		true_pixels.flip();
		return true_pixels;
	}

	@Override
	public boolean isCreated() {
		return created;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void loadWith(ByteBuffer pixels) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				pixels);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		created = true;
		textures.add(this);
	}
}
