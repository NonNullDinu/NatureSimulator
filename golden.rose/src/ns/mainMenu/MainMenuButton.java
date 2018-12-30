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

package ns.mainMenu;

import ns.interfaces.Action;
import ns.openglObjects.Texture;
import ns.ui.Button;
import org.lwjgl.util.vector.Vector2f;

public class MainMenuButton extends Button {
	private final Action action;
	private final Texture tex;

	public MainMenuButton(Vector2f center, Vector2f scale, Action action, Texture texture) {
		super(center, scale);
		this.action = action;
		this.tex = texture;
	}

	public void update() {
		if (super.clicked())
			action.execute();
	}

	public Texture getTex() {
		return tex;
	}

	public void executeAction() {
		action.execute();
	}
}