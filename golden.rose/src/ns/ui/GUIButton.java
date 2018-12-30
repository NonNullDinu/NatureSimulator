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

package ns.ui;

import ns.openglObjects.Texture;
import org.lwjgl.util.vector.Vector2f;

public class GUIButton extends Button {

	private final Texture texture;

	public GUIButton(Vector2f center, Vector2f scale, Texture tex) {
		super(center, scale);
		this.texture = tex;
	}

	public Texture getTexture() {
		return texture;
	}

//	@Override
//	public void render(GUIRenderer renderer, float[] args, int... argType) {
//		int i = 0;
//		float alpha = 1;
//		for(int arg : argType) {
//			if (arg == GUI.ALPHA) {
//				alpha = args[i];
//			}
//			i++;
//		}
//		renderer.render();
//	}
}