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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteMaterials extends ArrayList<ByteMaterial> {
	private static final long serialVersionUID = 7100141434010333203L;
	private static final int BYTES_PER_MATERIAL = 28; // 7 floats, or 28 bytes

	public ByteMaterials(List<Byte> bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_MATERIAL);
		for (int i = 0; i < bytes.size() / BYTES_PER_MATERIAL; i++) {
			for (int j = 0; j < BYTES_PER_MATERIAL; j++)
				buffer.put(bytes.get(i * BYTES_PER_MATERIAL + j));
			buffer.flip();
			super.add(new ByteMaterial(buffer));
			buffer.clear();
		}
	}
}