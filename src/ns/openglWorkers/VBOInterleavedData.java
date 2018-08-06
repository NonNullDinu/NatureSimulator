package ns.openglWorkers;

import java.nio.ByteBuffer;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import ns.openglObjects.VBO;

public class VBOInterleavedData {

	private ByteBuffer data;
	private VBOData[] dataArray;
	private Map<Integer, Integer> vbos;

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