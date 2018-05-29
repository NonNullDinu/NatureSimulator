package ns.shaders;

import org.lwjgl.opengl.GL20;

public class ColorQuadFillShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/colorQuad/quadVertex.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/colorQuad/quadFragment.glsl";
	
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");
	public UniformVec3 color = locator.locateUniformVec3("color");

	public ColorQuadFillShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
}