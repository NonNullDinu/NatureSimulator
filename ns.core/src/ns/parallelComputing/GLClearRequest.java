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

package ns.parallelComputing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class GLClearRequest extends Request {

	private final int mask;
	private final Vector3f color;

	public GLClearRequest(int mask, Vector3f color) {
		this.mask = mask;
		this.color = color;
	}

	@Override
	public void execute() {
		GL11.glClearColor(color.x, color.y, color.z, 1);
		GL11.glClear(mask);
	}
}