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

package ns.ui;

import ns.openglObjects.Texture;
import ns.renderers.GUIRenderer;
import org.lwjgl.util.vector.Vector2f;

public class GUITexture implements GUI {
	private final Texture texture;
	private Vector2f center;
	private Vector2f scale;

	public GUITexture(Vector2f center, Vector2f scale, Texture texture) {
		this.center = center;
		this.scale = scale;
		this.texture = texture;
	}

	public Vector2f getCenter() {
		return center;
	}

	public void setCenter(Vector2f center) {
		this.center = center;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public Texture getTexture() {
		return texture;
	}

	@Override
	public void render(GUIRenderer renderer, float[] args, int... argType) {

	}
}