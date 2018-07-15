package ns.shaders;

import org.lwjgl.opengl.GL20;

public class QuadShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "quad/quadVertex.glsl";
	private static final String FRAGMENT_SHADER = "quad/quadFragment.glsl";
	
	public UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");
	public UniformVec3 color = new UniformVec3("color");
	public UniformInt config = new UniformInt("config");
	public UniformFloat multFactor = new UniformFloat("multFactor");
	public UniformFloat z = new UniformFloat("z");
	
	public int COLOR_FILL = 1;
	public int TEXTURE_FILL = 2;
	public int TEXTURE_FILL_TRANSPARENCY_COLOR = 3;
	public int TEXTURE_FILL_ALPHA_01 = 4;

	public QuadShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(transformationMatrix, color, config, multFactor, z);
	}
}