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
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DataPacking {
	public static void packVertexDataf(int vertexCount, Map<Integer, Integer> current, VBOData... data) {
		List<float[]> dataList = new ArrayList<>();
		for (VBOData d : data) {
			float[] asArray = d.getDataf();
			if (asArray != null) {
				dataList.add(asArray);
			}
		}
		VBOInterleavedData interleavedData = new VBOInterleavedData(
				interleaveData(vertexCount, (float[][]) dataList.toArray(new float[dataList.size()][])), data, current);
		int vboId = GL15.glGenBuffers();
		VBO vbo = new VBO(vboId);
		interleavedData.store(vbo);
	}

	private static ByteBuffer interleaveData(int vertexCount, float[]... data) {
		int totalSize = 0;
		int[] lengths = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			int elementLength = data[i].length / vertexCount;
			lengths[i] = elementLength;
			totalSize += data[i].length;
		}
		float[] interleavedBuffer = new float[totalSize];
		int pointer = 0;
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < data.length; j++) {
				int elementLength = lengths[j];
				for (int k = 0; k < elementLength; k++) {
					interleavedBuffer[pointer++] = data[j][i * elementLength + k];
				}
			}
		}
		ByteBuffer buffer = BufferUtils.createByteBuffer(totalSize * 4);
		for (float f : interleavedBuffer)
			buffer.putFloat(f);
		buffer.flip();
		return buffer;
	}
}