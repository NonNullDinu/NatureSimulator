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
		List<Number[]> dataList = new ArrayList<>();
		for (VBOData d : data) {
			Number[] asArray = d.getData();
			if (asArray != null) {
				dataList.add(asArray);
			}
		}
		VBOInterleavedData interleavedData = new VBOInterleavedData(
				interleaveData(vertexCount, (Number[][]) dataList.toArray(new Number[dataList.size()][])), data, current);
		int vboId = GL15.glGenBuffers();
		VBO vbo = new VBO(vboId);
		interleavedData.store(vbo);
		return null;
	}

	public static ByteBuffer interleaveData(int vertexCount, Number[]... data) {
		int totalSize = 0;
		int[] lengths = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			int elementLength = data[i].length / vertexCount;
			lengths[i] = elementLength;
			totalSize += data[i].length;
		}
		Number[] interleavedBuffer = new Number[totalSize];
		int pointer = 0;
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < data.length; j++) {
				int elementLength = lengths[j];
				for (int k = 0; k < elementLength; k++) {
					interleavedBuffer[pointer++] = data[j][i * elementLength + k];
				}
			}
		}
		totalSize = 0;
		for(Number n : interleavedBuffer) {
			if(n instanceof Float)
				totalSize += 4;
			if(n instanceof Integer)
				totalSize += 4;
			if(n instanceof Byte)
				totalSize++;
		}
		ByteBuffer buffer = BufferUtils.createByteBuffer(totalSize);
		for(Number n : interleavedBuffer) {
			if(n instanceof Float)
				buffer.putFloat((Float) n);
			if(n instanceof Integer)
				buffer.putInt((Integer) n);
			if(n instanceof Byte)
				buffer.put((Byte) n);
		}
		return buffer;
	}
}