package ns.openglWorkers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import ns.openglObjects.VBO;

public class VBOData {
	private final float[] dataf;
	private final int[] datai;
	private final byte[] datab;
	
	private final int type;
	private int attNumber = -1;
	private boolean isIndices;
	private int gl_type;
	private int dimensions;
	private int usage = GL15.GL_STATIC_DRAW;
	
	public VBOData(float[] data) {
		dataf = data;
		datai = null;
		datab = null;
		type = 0;
		gl_type = GL11.GL_FLOAT;
	}
	
	public VBOData(int[] data) {
		dataf = null;
		datai = data;
		datab = null;
		type = 1;
		gl_type = GL11.GL_INT;
	}
	
	public VBOData(byte[] data) {
		dataf = null;
		datai = null;
		datab = data;
		type = 2;
		gl_type = GL11.GL_BYTE;
	}
	
	public VBOData withUsage(int usage) {
		this.usage = usage;
		return this;
	}
	
	public VBOData withDimensions(int dimensions) {
		this.dimensions = dimensions;
		return this;
	}
	
	public VBOData isIndices(boolean isIndices) {
		this.isIndices = isIndices;
		return this;
	}
	
	public VBOData withAttributeNumber(int attributeNumber) {
		this.attNumber = attributeNumber;
		return this;
	}
	
	public void store(VBO vbo) {
		int type = (isIndices ? GL15.GL_ELEMENT_ARRAY_BUFFER : GL15.GL_ARRAY_BUFFER);
		GL15.glBindBuffer(type, vbo.getId());
		if(this.type == 0) {
			FloatBuffer data = storeDataInFloatBuffer(dataf);
			GL15.glBufferData(type, data, usage);
			data.clear();
		}
		else if(this.type == 1) {
			IntBuffer data = storeDataInIntBuffer(datai);
			GL15.glBufferData(type, data, usage);
			data.clear();
		}
		else if(this.type == 2) {
			ByteBuffer data = storeDataInByteBuffer(datab);
			GL15.glBufferData(type, data, usage);
			data.clear();
		}
		if(attNumber != -1) {
			GL20.glVertexAttribPointer(attNumber, dimensions, this.gl_type, false, 0, 0);
			GL15.glBindBuffer(type, 0);
		}
	}

	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		return (FloatBuffer) BufferUtils.createFloatBuffer(data.length).put(data).flip();
	}

	private IntBuffer storeDataInIntBuffer(int[] data) {
		return (IntBuffer) BufferUtils.createIntBuffer(data.length).put(data).flip();
	}

	private ByteBuffer storeDataInByteBuffer(byte[] data) {
		return (ByteBuffer) BufferUtils.createByteBuffer(data.length).put(data).flip();
	}

	public boolean isIndices() {
		return isIndices;
	}

	public int getLength() {
		return (dataf != null ? dataf.length : (datab != null ? datab.length : datai.length));
	}

	public int getDimensions() {
		return dimensions;
	}

	public int getAttributeNumber() {
		return attNumber;
	}
}
