package ns.openglWorkers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import ns.openglObjects.VAO;

public class VBOUpdateData {
	private final float[] dataf;
	private final int[] datai;
	private final byte[] datab;
	private final int type;
	
	private int attToWriteTo;
	private long begin;

	public VBOUpdateData(float[] data) {
		this.dataf = data;
		this.datai = null;
		this.datab = null;
		type = 0;
	}

	public VBOUpdateData(int[] data) {
		this.dataf = null;
		this.datai = data;
		this.datab = null;
		type = 1;
	}

	public VBOUpdateData(byte[] data) {
		this.dataf = null;
		this.datai = null;
		this.datab = data;
		type = 2;
	}
	
	public VBOUpdateData withAttToWriteTo(int attToWriteTo) {
		this.attToWriteTo = attToWriteTo;
		return this;
	}
	
	public void updateWithin(VAO vao) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vao.getBuffers().get(attToWriteTo));
		if(type == 0) {
			FloatBuffer buffer = storeDataInFloatBuffer(dataf);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, begin, buffer);
			buffer.clear();
		}
		if(type == 1) {
			IntBuffer buffer = storeDataInIntBuffer(datai);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, begin, buffer);
			buffer.clear();
		}
		if(type == 2) {
			ByteBuffer buffer = storeDataInByteBuffer(datab);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, begin, buffer);
			buffer.clear();
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		return (FloatBuffer) BufferUtils.createFloatBuffer(data.length).put(data).flip();
	}

	private static IntBuffer storeDataInIntBuffer(int[] data) {
		return (IntBuffer) BufferUtils.createIntBuffer(data.length).put(data).flip();
	}

	private static ByteBuffer storeDataInByteBuffer(byte[] data) {
		return (ByteBuffer) BufferUtils.createByteBuffer(data.length).put(data).flip();
	}

	public VBOUpdateData withBegin(long begin) {
		this.begin = begin;
		return this;
	}
}