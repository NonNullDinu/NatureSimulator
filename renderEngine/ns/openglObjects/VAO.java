package ns.openglObjects;

import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VAO implements IOpenGLObject, IRenderable {
	private static int nextId = 1;
	
	private int id;
	private int vertexCount;
	private Map<Integer, Integer> vbos;

	private boolean hasIndices;

	public VAO(int id, int vertexCount, Map<Integer, Integer> current, boolean hasIndices) {
		this.id = id;
		this.vertexCount = vertexCount;
		this.vbos = current;
		this.hasIndices = hasIndices;
	}

	public VAO() {
	}

	public int getId() {
		return id;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
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

	public void bind() {
		GL30.glBindVertexArray(id);
		for (int attn : vbos.keySet())
			GL20.glEnableVertexAttribArray(attn);
	}

	public void unbind() {
		for (int attn : vbos.keySet())
			GL20.glDisableVertexAttribArray(attn);
		GL30.glBindVertexArray(0);
	}

	public static int getNext() {
		return nextId;
	}

	public static void addOneToNext() {
		nextId = nextId + 1;
	}

	@Override
	public VAO create() {
		return this;
	}

	@Override
	public void delete() {
		GL30.glDeleteVertexArrays(id);
		for (int buffer : vbos.values())
			GL15.glDeleteBuffers(buffer);
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void render() {
		bind();
		batchRenderCall();
		unbind();
	}

	@Override
	public void batchRenderCall() {
		if(hasIndices)
			GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
		else
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
	}

	public boolean hasIndices() {
		return hasIndices;
	}
}