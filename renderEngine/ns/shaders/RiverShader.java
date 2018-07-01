package ns.shaders;

import org.lwjgl.opengl.GL20;

public class RiverShader extends ShaderProgram {
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");
	public UniformVec3 color = locator.locateUniformVec3("color");
	public UniformMat4 projectionMatrix = locator.locateUniformMat4("projectionMatrix");
	public UniformMat4 viewMatrix = locator.locateUniformMat4("viewMatrix");
	public UniformInt loc = locator.locateUniformInt("loc");

	public RiverShader() {
		super(new Shader("shaders/river/riverVertex.glsl", GL20.GL_VERTEX_SHADER),
				new Shader("shaders/river/riverFragment.glsl", GL20.GL_FRAGMENT_SHADER));
	}
}