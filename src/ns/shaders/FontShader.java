package ns.shaders;

import org.lwjgl.opengl.GL20;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "font/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "font/fontFragment.glsl";

	public UniformVec3 color = new UniformVec3("color");
	public UniformVec2 translation = new UniformVec2("translation");
	public UniformFloat alphaCoef = new UniformFloat("alpha");

	public FontShader() {
		super(new Shader(VERTEX_FILE, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_FILE, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(color, translation, alphaCoef);
	}
}