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

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class Button {
	private final Vector2f center;
	private final Vector2f scale;

	public Button(Vector2f center, Vector2f scale) {
		this.center = center;
		this.scale = scale;
	}

	public boolean isMouseOver() {
		double normalizedX = -1.0 + 2.0 * (double) Mouse.getX() / Display.getWidth();
		double normalizedY = -(1.0 - 2.0 * (double) Mouse.getY() / Display.getHeight());
		Vector2f relToButtonLocation = Vector2f.sub(center, new Vector2f((float) normalizedX, (float) normalizedY), null);
		return (Math.abs(relToButtonLocation.x) <= scale.x && Math.abs(relToButtonLocation.y) <= scale.y);
	}

	public boolean clicked() {
		return isMouseOver() && Mouse.isButtonDown(0);
	}

	public Vector2f getCenter() {
		return center;
	}

	public Vector2f getScale() {
		return scale;
	}
}