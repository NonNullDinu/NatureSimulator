package ns.shaders;

import org.lwjgl.opengl.GL20;

public class QuadShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "quad/quadVertex.glsl";
	private static final String FRAGMENT_SHADER = "quad/quadFragment.glsl";

	public final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");
	public final UniformVec3 color = new UniformVec3("color");
	public final UniformInt config = new UniformInt("config");
	public final UniformFloat multFactor = new UniformFloat("multFactor");
	public final UniformFloat z = new UniformFloat("z");

	public final int COLOR_FILL = 1;
	public final int TEXTURE_FILL = 2;
	public final int TEXTURE_FILL_TRANSPARENCY_COLOR = 3;
	public final int TEXTURE_FILL_ALPHA_01 = 4;

	public QuadShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(transformationMatrix, color, config, multFactor, z);
	}
}