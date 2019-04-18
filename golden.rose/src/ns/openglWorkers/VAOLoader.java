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

import ns.openglObjects.VAO;
import ns.openglObjects.VBO;
import ns.parallelComputing.*;
import ns.utils.GU;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.lang.Thread;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class VAOLoader {
	private static final Map<Integer, Map<Integer, Integer>> vaos = new HashMap<>();
	private static final List<Integer> vbos = new ArrayList<>();

	public static VAO storeDataInVAO(VBOData... data) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
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
			CreateVAORequest req = new CreateVAORequest(target, data);
			GU.sendRequestToMainThread(req);
			return target;
		}
	}

	/**
	 * Not working like it must work
	 *
	 * @param data
	 * @return
	 */
	@Deprecated
	public static VAO storeAndPackDataInVAO(VBOData... data) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			int vaoId = GL30.glGenVertexArrays(), vertexCount = -1;
			boolean hasIndices = false;
			Map<Integer, Integer> current = new HashMap<>();
			vaos.put(vaoId, current);
			GL30.glBindVertexArray(vaoId);
			List<VBOData> toStaticVBOData = new ArrayList<>();
			for (VBOData d : data) {
				if (d.getUsage() == GL15.GL_STATIC_DRAW && !d.isIndices()) {
					toStaticVBOData.add(d);
				} else {
					int vboId = GL15.glGenBuffers();
					current.put(d.getAttributeNumber(), vboId);
					VBO vbo = new VBO(vboId);
					d.store(vbo);
					if (d.isIndices()) {
						hasIndices = true;
						vertexCount = d.getLength();
					}
				}
			}
			if (vertexCount == -1)
				vertexCount = data[0].getLength() / data[0].getDimensions();
			DataPacking.packVertexDataf(vertexCount, current, data);
			GL30.glBindVertexArray(0);
			return new VAO(vaoId, vertexCount, current, hasIndices);
		} else {
			VAO target = new VAO();
			GU.sendRequestToMainThread(new CreateAndPackVAORequest(data, target));
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
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vaos.get(model.getID()).get(attn));
			FloatBuffer dt = storeDataInFloatBuffer(data);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, dt);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			GU.sendRequestToMainThread(new VAOUpdateRequest(model, new VBOUpdateData(data).withAttToWriteTo(attn)));
		}
	}

	public static void replace(VAO model, int attn, int[] data) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vaos.get(model.getID()).get(attn));
			IntBuffer dt = storeDataInIntBuffer(data);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, dt);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			GU.sendRequestToMainThread(new VAOUpdateRequest(model, new VBOUpdateData(data).withAttToWriteTo(attn)));
		}
	}

	public static void replace(VAO model, int attn, byte[] data) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vaos.get(model.getID()).get(attn));
			ByteBuffer dt = storeDataInByteBuffer(data);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, dt);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			GU.sendRequestToMainThread(new VAOUpdateRequest(model, new VBOUpdateData(data).withAttToWriteTo(attn)));
		}
	}

	public static void replace(VAO model, int attn, float[] data, long begin) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			new VBOUpdateData(data).withAttToWriteTo(attn).withBegin(begin).updateWithin(model);
		} else {
			GU.sendRequestToMainThread(
					new VAOUpdateRequest(model, new VBOUpdateData(data).withBegin(begin).withAttToWriteTo(attn)));
		}
	}

	public static void replace(VAO model, int attn, int[] data, long begin) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			new VBOUpdateData(data).withAttToWriteTo(attn).withBegin(begin).updateWithin(model);
		} else {
			GU.sendRequestToMainThread(
					new VAOUpdateRequest(model, new VBOUpdateData(data).withBegin(begin).withAttToWriteTo(attn)));
		}
	}

	public static void replace(VAO model, int attn, byte[] data, long begin) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			new VBOUpdateData(data).withAttToWriteTo(attn).withBegin(begin).updateWithin(model);
		} else {
			GU.sendRequestToMainThread(
					new VAOUpdateRequest(model, new VBOUpdateData(data).withBegin(begin).withAttToWriteTo(attn)));
		}
	}

	public static void createVAOAndStore(VAO vao, VBOData... data) {
		int vaoId = GL30.glGenVertexArrays(), vertexCount = 0;
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
		if (!hasIndices)
			vertexCount = data[0].getLength() / data[0].getDimensions();
		GL30.glBindVertexArray(0);
		vao.setId(vaoId);
		vao.setVertexCount(vertexCount);
		vao.setVbos(current);
		vao.setHasIndices(hasIndices);
	}

	/**
	 * Not working like it must work
	 *
	 * @param vao
	 * @param data
	 */
	@Deprecated
	public static void createVAOAndStorePack(VAO vao, VBOData... data) {
		int vaoId = GL30.glGenVertexArrays(), vertexCount = -1;
		boolean hasIndices = false;
		Map<Integer, Integer> current = new HashMap<>();
		vaos.put(vaoId, current);
		GL30.glBindVertexArray(vaoId);
		List<VBOData> toStaticVBOData = new ArrayList<>();
		for (VBOData d : data) {
			if (d.getUsage() == GL15.GL_STATIC_DRAW && !d.isIndices()) {
				toStaticVBOData.add(d);
			} else {
				int vboId = GL15.glGenBuffers();
				current.put(d.getAttributeNumber(), vboId);
				VBO vbo = new VBO(vboId);
				d.store(vbo);
				if (d.isIndices()) {
					hasIndices = true;
					vertexCount = d.getLength();
				}
			}
		}
		if (vertexCount == -1)
			vertexCount = data[0].getLength() / data[0].getDimensions();
		DataPacking.packVertexDataf(vertexCount, current, data);
		GL30.glBindVertexArray(0);
		vao.setId(vaoId);
		vao.setVertexCount(vertexCount);
		vao.setVbos(current);
		vao.setHasIndices(hasIndices);
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		return BufferUtils.createFloatBuffer(data.length).put(data).flip();
	}

	private static IntBuffer storeDataInIntBuffer(int[] data) {
		return BufferUtils.createIntBuffer(data.length).put(data).flip();
	}

	private static ByteBuffer storeDataInByteBuffer(byte[] data) {
		return BufferUtils.createByteBuffer(data.length).put(data).flip();
	}

	public static void replace(VAO model, int attn, float[] data, List<Integer> changes, int dimensions) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, model.getBuffers().get(attn));
			FloatBuffer buffer = null;
			for (int i : changes) {
				long offset = i * dimensions * 4;
				if (dimensions == 2)
					buffer = storeDataInFloatBuffer(new float[]{data[i * 2], data[i * 2 + 1]});
				if (dimensions == 3)
					buffer = storeDataInFloatBuffer(new float[]{data[i * 3], data[i * 3 + 1], data[i * 3 + 2]});
				if (dimensions == 4)
					buffer = storeDataInFloatBuffer(
							new float[]{data[i * 4], data[i * 4 + 1], data[i * 4 + 2], data[i * 4 + 3]});
				GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, Objects.requireNonNull(buffer));
				buffer.clear();
			}
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			GU.sendRequestToMainThread(new VBOUpdateRequest(model, attn, data, changes, dimensions));
		}
	}

	public static void recreateAndReplace(VAO model, int attn, float[] data, int usage) {
		if (java.lang.Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			int vbo = vaos.get(model.getID()).get(attn);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL43.glInvalidateBufferData(vbo);
			FloatBuffer dt = storeDataInFloatBuffer(data);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dt, usage);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			GU.sendRequestToMainThread(new VBORecreateAndReplaceRequest(model, attn, data, usage));
		}
	}

	public static void recreateAndReplace(VAO model, int attn, byte[] data, int usage) {
		if (Thread.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			int vbo = vaos.get(model.getID()).get(attn);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL43.glInvalidateBufferData(vbo);
			ByteBuffer dt = storeDataInByteBuffer(data);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dt, usage);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			GU.sendRequestToMainThread(new VBORecreateAndReplaceRequest(model, attn, data, usage));
		}
	}
}