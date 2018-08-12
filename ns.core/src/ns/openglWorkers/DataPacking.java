package ns.openglWorkers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import ns.openglObjects.VBO;

public class DataPacking {
	public static VBO packVertexDataf(int vertexCount, Map<Integer, Integer> current, VBOData... data) {
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
		return vbo;
	}

	public static ByteBuffer interleaveData(int vertexCount, float[]... data) {
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
		for(float f : interleavedBuffer)
			buffer.putFloat(f);
		buffer.flip();
		return buffer;
	}
}