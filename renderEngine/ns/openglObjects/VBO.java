package ns.openglObjects;

import org.lwjgl.opengl.GL15;

public class VBO implements IOpenGLObject {
	private final int id;
	private boolean created = false;

	public VBO(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public IOpenGLObject create() {
		created = true;
		return this;
	}

	@Override
	public void delete() {
		GL15.glDeleteBuffers(id);
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public boolean isCreated() {
		return created;
	}
}
