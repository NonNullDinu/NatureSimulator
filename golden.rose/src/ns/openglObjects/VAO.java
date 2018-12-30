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

package ns.openglObjects;

import ns.interfaces.Action;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VAO implements IOpenGLObject, IRenderable {
	private static int nextId = 1;
	private static Action executeRequests;
	private final List<Integer> currentlyBound;
	private int id;
	private int vertexCount;
	private Map<Integer, Integer> vbos;
	private boolean hasIndices;
	private boolean created = false;

	public VAO(int id, int vertexCount, Map<Integer, Integer> current, boolean hasIndices) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.vbos = current;
		this.hasIndices = hasIndices;
		this.created = true;
		currentlyBound = new ArrayList<>();
	}

	public VAO() {
		currentlyBound = new ArrayList<>();
	}

	public static void init(Action action) {
		executeRequests = action;
	}

	public static int getNext() {
		return nextId;
	}

	public static void addOneToNext() {
		nextId = nextId + 1;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setVbos(Map<Integer, Integer> vbos) {
		this.vbos = vbos;
	}

	public void setHasIndices(boolean hasIndices) {
		this.hasIndices = hasIndices;
	}

	public void cleanUp() {
		GL30.glDeleteVertexArrays(id);
		for (int buffer : vbos.values())
			GL15.glDeleteBuffers(buffer);
	}

	public Map<Integer, Integer> getBuffers() {
		return vbos;
	}

	public void bind(int... attributes) {
		if (vbos == null)
			executeRequests.execute();
		GL30.glBindVertexArray(id);
		for (int attn : attributes) {
			GL20.glEnableVertexAttribArray(attn);
			currentlyBound.add(attn);
		}
	}

	public void bindAll() {
		if (vbos == null)
			executeRequests.execute();
		GL30.glBindVertexArray(id);
		for (int attn : vbos.keySet())
			if (attn != -1) {
				GL20.glEnableVertexAttribArray(attn);
				currentlyBound.add(attn);
			}
	}

	public void unbind() {
		for (int attn : currentlyBound)
			if (attn != -1)
				GL20.glDisableVertexAttribArray(attn);
		currentlyBound.clear();
		GL30.glBindVertexArray(0);
	}

	@Override
	public VAO create() {
		created = true;
		return this;
	}

	@Override
	public void delete() {
		GL30.glDeleteVertexArrays(id);
		for (int buffer : vbos.values())
			GL15.glDeleteBuffers(buffer);
		created = false;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void render() {
		bind((Integer[]) vbos.keySet().toArray());
		batchRenderCall();
		unbind();
	}

	protected void bind(Integer[] array) {
		GL30.glBindVertexArray(id);
		for (int attn : array) {
			GL20.glEnableVertexAttribArray(attn);
			currentlyBound.add(attn);
		}
	}

	@Override
	public void batchRenderCall() {
		if (hasIndices)
			GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
		else
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
	}

	public boolean hasIndices() {
		return hasIndices;
	}

	@Override
	public boolean isCreated() {
		return created;
	}
}