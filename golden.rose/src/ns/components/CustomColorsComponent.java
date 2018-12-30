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

package ns.components;

import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class CustomColorsComponent implements IComponent {
	private static final long serialVersionUID = 3019699690479243107L;

	private final List<Vector3f> colors;

	CustomColorsComponent(List<Vector3f> colors) {
		this.colors = colors;
	}

	public List<Vector3f> getColors() {
		return colors;
	}

	@Override
	public IComponent copy() {
		return new CustomColorsComponent(colors);
	}
}