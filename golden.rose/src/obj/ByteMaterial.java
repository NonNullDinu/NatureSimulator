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

package obj;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.ByteBuffer;

public class ByteMaterial {
	private final float[] dataf;

	public ByteMaterial(ByteBuffer buffer) {
		this.dataf = new float[buffer.capacity() / 4];
		for (int i = 0; i < dataf.length; i++)
			dataf[i] = buffer.getFloat();
	}

	public Vector3f getColor() {
		return new Vector3f(dataf[0], dataf[1], dataf[2]);
	}

	public Vector3f getIndicators() {
		return new Vector3f(dataf[3], dataf[4], dataf[5]);
	}

	public Vector4f getData() {
		return new Vector4f(dataf[6], 0, 0, 0);
	}
}