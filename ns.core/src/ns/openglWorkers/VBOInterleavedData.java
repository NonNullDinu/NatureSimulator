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

package ns.openglWorkers;

import ns.openglObjects.VBO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.util.Map;

class VBOInterleavedData {

	private final ByteBuffer data;
	private final VBOData[] dataArray;
	private final Map<Integer, Integer> vbos;

	public VBOInterleavedData(ByteBuffer interleavedData, VBOData[] data, Map<Integer, Integer> vbos) {
		this.data = interleavedData;
		this.dataArray = data;
		this.vbos = vbos;
	}

	public void store(VBO vbo) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo.getId());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		data.clear();
		int offset = 0;
		int stride = 0;
		for (VBOData dt : dataArray)
			stride += dt.getStride();
		for (VBOData dt : dataArray) {
			int size = dt.getStride();
			GL20.glVertexAttribPointer(dt.getAttributeNumber(), dt.getDimensions(), GL11.GL_FLOAT, false, stride,
					offset);
			offset += size;
			vbos.put(dt.getAttributeNumber(), vbo.getID());
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
}