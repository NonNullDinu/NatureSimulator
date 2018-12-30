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

import ns.interfaces.Action;
import ns.interfaces.UIMenu;
import ns.renderers.GUIRenderer;
import ns.ui.GUIButton;
import ns.utils.GU;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class Options implements UIMenu {
	private final List<Option> options;
	private final GUIButton back;
	private final Action action;

	public Options(List<Option> options, GUIButton back, Action backClicked) {
		this.options = options;
		this.back = back;
		this.action = backClicked;
	}

	public void update() {
		Vector2f mp = GU.normalizedMousePos();
		for (Option option : options) {
			Vector2f pos = option.loc(mp);
			option.checkAndClick(pos);
		}
		if (back.clicked()) {
			action.execute();
		}
	}

	public void render() {
		for (Option option : options) {
			option.render();
		}
		GUIRenderer.instance.render(back);
	}
}