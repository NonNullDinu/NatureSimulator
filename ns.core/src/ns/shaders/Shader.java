package ns.shaders;

import ns.openglObjects.IOpenGLObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader implements IOpenGLObject {
	private String src;
	private int ID;
	private boolean created = false;
	private final String name;
	final int type;

	public Shader(String name, int type) {
		while (this.src == null)
			this.src = ShaderLib.getSource("gameData/shaders/" + name);
		this.name = name;
		this.type = type;
	}

	public void bindToProgram(ShaderProgram program) {
		GL20.glAttachShader(program.getID(), this.ID);
	}

	public String getSource() {
		return src;
	}

	@Override
	public Shader create() {
		created = true;
		ID = GL20.glCreateShader(type);
		GL20.glShaderSource(ID, src);
		GL20.glCompileShader(ID);
		if (GL20.glGetShaderi(ID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
			System.err.println("Could not compile shader " + name + ", log:" + GL20.glGetShaderInfoLog(ID, 200));
			System.exit(-1);
		}
		return this;
	}

	@Override
	public void delete() {
		GL20.glDeleteShader(ID);
		created = false;
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public boolean isCreated() {
		return created;
	}
}