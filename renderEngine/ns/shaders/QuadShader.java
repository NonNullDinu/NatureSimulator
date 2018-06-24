package ns.shaders;

import org.lwjgl.opengl.GL20;

public class QuadShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "shaders/quad/quadVertex.glsl";
	private static final String FRAGMENT_SHADER = "shaders/quad/quadFragment.glsl";
	
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");
	public UniformVec3 color = locator.locateUniformVec3("color");
	public UniformInt config = locator.locateUniformInt("config");
	public UniformFloat multFactor = locator.locateUniformFloat("multFactor", false);
	public UniformFloat z = locator.locateUniformFloat("z", false);
	
	public int COLOR_FILL = 1;
	public int TEXTURE_FILL = 2;
	public int TEXTURE_FILL_TRANSPARENCY_COLOR = 3;
	public int TEXTURE_FILL_ALPHA_01 = 4;

	public QuadShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
}