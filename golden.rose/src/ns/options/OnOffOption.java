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

package ns.options;

import ns.fontMeshCreator.GUIText;
import ns.fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector2f;

public class OnOffOption extends Option {
	private boolean on;

	public OnOffOption(Vector2f position, Vector2f scale, GUIText text) {
		super(position, scale, text);
	}

	@Override
	protected void click(float x, float y) {
		TextMaster.delete(text);
		text.setText(text.getTextString().replace((on ? "On" : "Off"), (!on ? "On" : "Off")));
		on = !on;
		TextMaster.loadText(text);
	}

	@Override
	public void render() {
		TextMaster.add(text);
		TextMaster.render();
		TextMaster.remove(text);
	}
}