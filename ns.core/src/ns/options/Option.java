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

package ns.options;

import ns.fontMeshCreator.GUIText;
import ns.fontRendering.TextMaster;
import ns.utils.GU;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public abstract class Option {
	final GUIText text;
	private final Vector2f position;
	private final Vector2f scale;

	public Option(Vector2f position, Vector2f scale, GUIText text) {
		this.position = position;
		this.scale = scale;
		this.text = text;
		TextMaster.loadText(text);
	}

	void checkAndClick(Vector2f pos) {
		if (Math.abs(pos.x) <= scale.x && Math.abs(pos.y) <= scale.y && Mouse.isButtonDown(0) && !GU.prevFrameClicked)
			click(pos.x - scale.x, pos.y - scale.y);
	}

	protected abstract void click(float x, float y);

	Vector2f loc(Vector2f mouse) {
		return Vector2f.sub(position, new Vector2f(mouse.x, mouse.y), null);
	}

	public abstract void render();
}