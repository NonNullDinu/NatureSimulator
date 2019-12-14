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

package ns.mainMenu;

import ns.entities.Entity;
import ns.interfaces.UIMenu;
import ns.utils.GU;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class MainMenu implements UIMenu {
	public static MainMenu instance;
	private final List<MainMenuButton> buttons;
	private final Entity DNA;
	private final Vector4f dnaLocation;

	public MainMenu(List<MainMenuButton> buttons, Entity DNA, Vector4f dnaLocation) {
		instance = this;
		this.buttons = buttons;
		this.DNA = DNA;
		this.dnaLocation = dnaLocation;
	}

	public void update() {
		int idx = 0;
		for (MainMenuButton button : buttons) {
			if (button.isMouseOver()) {
				DNA.setRotY(idx * 36f);
                if (GU.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					button.executeAction();
				}
			}
			idx++;
		}
	}

	public List<MainMenuButton> getButtons() {
		return buttons;
	}

	public Entity getDNA() {
		return DNA;
	}

	public Vector4f getDnaLocation() {
		return dnaLocation;
	}
}