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

import org.lwjgl.util.vector.Vector2f;

public class PlantLifeComponent extends LifeComponent {
	private static final long serialVersionUID = 8859820823277404834L;

	private Vector2f lim;

	PlantLifeComponent(float totalLifespan) {
		super(totalLifespan);
	}

	public PlantLifeComponent withLimits(Vector2f limits) {
		this.lim = limits;
		return this;
	}

	@Override
	public boolean isWithinHeightLimits(float y) {
		return y >= lim.x && y <= lim.y;
	}

	public Vector2f getLimits() {
		return lim;
	}
}