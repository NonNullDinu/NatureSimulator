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

package ns.entities;

import ns.time.DayNightCycle;
import ns.utils.GU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Sun extends Light {
	public Sun(Vector3f direction, Vector3f color, Vector2f bias) {
		super(direction, color, bias);
	}

	public void update() {
		double angle = Math.toRadians(GU.time.t / DayNightCycle.H_S_DURATION % 24f / 24f * 360f + 90f /*??*/);
		dir.x = (float) Math.cos(angle);
		dir.y = (float) Math.sin(angle);
	}
}
