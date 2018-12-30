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

package ns.derrivedOpenGLObjects;

import ns.openglObjects.Texture;
import org.lwjgl.util.vector.Vector2f;

public class FlareTexture {
	private final boolean hasRotation;
	private Vector2f position;
	private float scale;
	private Texture texture;
	private float rotation;
	private float rotOff;

	public FlareTexture(Texture texture, float scale) {
		this.texture = texture;
		this.scale = scale;
		this.hasRotation = false;
	}

	public FlareTexture(Texture texture, float scale, float rotOff) {
		this.texture = texture;
		this.scale = scale;
		this.hasRotation = true;
		this.rotation = 0;
		this.rotOff = rotOff;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation + rotOff;
	}

	public boolean hasRotation() {
		return hasRotation;
	}
}