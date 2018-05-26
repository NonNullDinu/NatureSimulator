package ns.openglWorkers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import ns.openglObjects.VAO;
import ns.openglObjects.VBO;
import ns.parallelComputing.CreateVAORequest;
import ns.parallelComputing.ThreadMaster;
import ns.parallelComputing.VAOUpdateRequest;

public class DataPacking {
	private static final Map<Integer, Map<Integer, Integer>> vaos = new HashMap<>();
	private static final List<Integer> vbos = new ArrayList<>();

	public static VAO storeDataInVAO(VBOData... data) {
		if (Thread.currentThread().getName().equals("main thread")) {
			int vaoId = GL30.glGenVertexArrays(), vertexCount = -1;
			boolean hasIndices = false;
			Map<Integer, Integer> current = new HashMap<>();
			vaos.put(vaoId, current);
			GL30.glBindVertexArray(vaoId);
			for (VBOData d : data) {
				int vboId = GL15.glGenBuffers();
				current.put(d.getAttributeNumber(), vboId);
				VBO vbo = new VBO(vboId);
				d.store(vbo);
				if (d.isIndices()) {
					hasIndices = true;
					vertexCount = d.getLength();
				}
			}
			if (vertexCount == -1)
				vertexCount = data[0].getLength() / data[0].getDimensions();
			GL30.glBindVertexArray(0);
			return new VAO(vaoId, vertexCount, current, hasIndices);
		} else {
			VAO target = new VAO();
			ThreadMaster.getThread("main thread")
					.setToCarryOutRequest(new CreateVAORequest("create vao", data, target));
			return target;
		}
	}

	public static void cleanUp() {
		for (int vao : vaos.keySet()) {
			GL30.glDeleteVertexArrays(vao);
			for (int vbo : vaos.get(vao).values())
				GL15.glDeleteBuffers(vbo);
		}
		for (int vbo : vbos)
			GL15.glDeleteBuffers(vbo);
	}

	public static void replace(VAO model, int attn, float[] data) {
		if (Thread.currentThread().getName().equals("main thread")) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vaos.get(model.getId()).get(attn));
			FloatBuffer dt = storeDataInFloatBuffer(data);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, dt);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			ThreadMaster.getThread("main thread").setToCarryOutRequest(
					new VAOUpdateRequest("update vao", new VBOUpdateData(data).withAttToWriteTo(attn), model));
		}
	}

	public static void replace(VAO model, int attn, int[] data) {
		if (Thread.currentThread().getName().equals("main thread")) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vaos.get(model.getId()).get(attn));
			IntBuffer dt = storeDataInIntBuffer(data);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, dt);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			ThreadMaster.getThread("main thread").setToCarryOutRequest(
					new VAOUpdateRequest("update vao", new VBOUpdateData(data).withAttToWriteTo(attn), model));
		}
	}

	public static void replace(VAO model, int attn, byte[] data) {
		if (Thread.currentThread().getName().equals("main thread")) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vaos.get(model.getId()).get(attn));
			ByteBuffer dt = storeDataInByteBuffer(data);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, dt);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			ThreadMaster.getThread("main thread").setToCarryOutRequest(
					new VAOUpdateRequest("update vao", new VBOUpdateData(data).withAttToWriteTo(attn), model));
		}
	}

	public static void replace(VAO model, int attn, float[] data, long begin) {
		if (Thread.currentThread().getName().equals("main thread")) {
			new VBOUpdateData(data).withAttToWriteTo(attn).withBegin(begin).updateWithin(model);
		} else {
			ThreadMaster.getThread("main thread").setToCarryOutRequest(new VAOUpdateRequest("update vao",
					new VBOUpdateData(data).withBegin(begin).withAttToWriteTo(attn), model));
		}
	}

	public static void replace(VAO model, int attn, int[] data, long begin) {
		if (Thread.currentThread().getName().equals("main thread")) {
			new VBOUpdateData(data).withAttToWriteTo(attn).withBegin(begin).updateWithin(model);
		} else {
			ThreadMaster.getThread("main thread").setToCarryOutRequest(new VAOUpdateRequest("update vao",
					new VBOUpdateData(data).withBegin(begin).withAttToWriteTo(attn), model));
		}
	}

	public static void replace(VAO model, int attn, byte[] data, long begin) {
		if (Thread.currentThread().getName().equals("main thread")) {
			new VBOUpdateData(data).withAttToWriteTo(attn).withBegin(begin).updateWithin(model);
		} else {
			ThreadMaster.getThread("main thread").setToCarryOutRequest(new VAOUpdateRequest("update vao",
					new VBOUpdateData(data).withBegin(begin).withAttToWriteTo(attn), model));
		}
	}

	public static void createVAOAndStore(VAO vao, VBOData... data) {
		int vaoId = GL30.glGenVertexArrays(), vertexCount = -1;
		boolean hasIndices = false;
		Map<Integer, Integer> current = new HashMap<>();
		vaos.put(vaoId, current);
		GL30.glBindVertexArray(vaoId);
		for (VBOData d : data) {
			int vboId = GL15.glGenBuffers();
			current.put(d.getAttributeNumber(), vboId);
			VBO vbo = new VBO(vboId);
			d.store(vbo);
			if (d.isIndices()) {
				vertexCount = d.getLength();
				hasIndices = true;
			}
		}
		if (vertexCount == -1)
			vertexCount = data[0].getLength() / data[0].getDimensions();
		GL30.glBindVertexArray(0);
		vao.setId(vaoId);
		vao.setVertexCount(vertexCount);
		vao.setVbos(current);
		vao.setHasIndices(hasIndices);
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

	public static void replace(VAO model, int attn, float[] data, List<Integer> changes, int dimensions) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, model.getBuffers().get(attn));
		FloatBuffer buffer = null;
		for (int i : changes) {
			long offset = i * dimensions * 4;
			if (dimensions == 2)
				buffer = storeDataInFloatBuffer(new float[] { data[i * 2], data[i * 2 + 1] });
			if (dimensions == 3)
				buffer = storeDataInFloatBuffer(new float[] { data[i * 3], data[i * 3 + 1], data[i * 3 + 2] });
			if (dimensions == 4)
				buffer = storeDataInFloatBuffer(
						new float[] { data[i * 4], data[i * 4 + 1], data[i * 4 + 2], data[i * 4 + 3] });
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, buffer);
			buffer.clear();
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
}
