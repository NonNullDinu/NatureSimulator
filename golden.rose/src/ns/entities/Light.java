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

package ns.entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Light {
	public final Vector3f dir;
	public final Vector3f color;
	public final Vector2f bias;

	public Light(Vector3f dir, Vector3f color, Vector2f bias) {
		this.dir = dir;
		this.color = color;
		this.bias = bias;
	}

	public Vector3f getDir() {
		return dir;
	}

	public Vector3f getColor() {
		return color;
	}

	public Vector2f getBias() {
		return bias;
	}
}