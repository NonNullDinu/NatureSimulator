/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
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

package ns.parallelComputing;

import ns.openglObjects.Texture;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class TextureCreateRequest extends Request {

	private final Texture target;
	private final ByteBuffer pixels;

	public TextureCreateRequest(Texture target, ByteBuffer pixels) {
		this.target = target;
		this.pixels = pixels;
	}

	@Override
	public void execute() {
		int id = GL11.glGenTextures();
		target.setId(id);
		target.loadWith(pixels);
	}
}