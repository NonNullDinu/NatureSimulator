package ns.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import ns.openglObjects.IOpenGLObject;

public abstract class ShaderProgram implements IOpenGLObject {
	private final int programId;
	private final int vertexShaderId;
	private final int fragmentShaderId;
	protected StringBuffer src;
	protected UniformLocator locator = new UniformLocator(this);

	public ShaderProgram(String VERTEX_SHADER, String FRAGMENT_SHADER) {
		programId = GL20.glCreateProgram();
		src = new StringBuffer();
		vertexShaderId = createShader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER);
		fragmentShaderId = createShader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER);
		GL20.glAttachShader(programId, vertexShaderId);
		GL20.glAttachShader(programId, fragmentShaderId);
		preLink();
		GL20.glLinkProgram(programId);
		GL20.glValidateProgram(programId);
		postLink();
	}

	protected void preLink() {
	}

	protected void postLink() {
	}

	protected int variableType(String name) {
		String[] src = this.src.toString().split("\n");
		String declarationLine = null;
		for (String s : src) {
			String[] lineParts = s.split(" ");
			if (lineParts[lineParts.length - 1].equals(name + ";")) {
				declarationLine = s;
			}
		}
		if (declarationLine == null)
			return -1;
		String type = declarationLine.split(" ")[declarationLine.split(" ").length - 2];
		switch (type) {
		case "float":
			return UniformVar.TYPE_FLOAT;
		case "vec2":
			return UniformVar.TYPE_VEC2;
		case "vec3":
			return UniformVar.TYPE_VEC3;
		case "vec4":
			return UniformVar.TYPE_VEC4;
		case "mat4":
			return UniformVar.TYPE_MAT4;
		case "int":
			return UniformVar.TYPE_INT;
		case "bool":
			return UniformVar.TYPE_BOOL;
		case "sampler2D":
			return UniformVar.TYPE_SAMPLER_2D;
		case "Light":
			return UniformVar.TYPE_LIGHT;
		default:
			return -1;
		}
	}

	protected int getLocation(String name) {
		return GL20.glGetUniformLocation(programId, name);
	}

	private int createShader(String shader, int shaderType) {
		String source = "";
		String shaderSource = ShaderLib.getSource(shader);
		source += shaderSource;
		src.append(shaderSource);
		int shaderId = GL20.glCreateShader(shaderType);
		GL20.glShaderSource(shaderId, source);
		GL20.glCompileShader(shaderId);
		if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error compiling shader :" + shader + ", log:");
			System.err.print(GL20.glGetShaderInfoLog(shaderId, 500));
			System.exit(-1);
		}
		return shaderId;
	}

	public void start() {
		GL20.glUseProgram(programId);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(programId, vertexShaderId);
		GL20.glDetachShader(programId, fragmentShaderId);
		GL20.glDeleteShader(vertexShaderId);
		GL20.glDeleteShader(fragmentShaderId);
		GL20.glDeleteProgram(programId);
	}
	
	public ShaderProgram create() {
		return this;
	}
	
	public void delete() {
		cleanUp();
	}
	
	public int getID() {
		return programId;
	}
}
