package ns.shaders;

import org.lwjgl.opengl.GL20;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/font/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "shaders/font/fontFragment.glsl";

	public UniformVec3 color = locator.locateUniformVec3("color");
	public UniformVec2 translation = locator.locateUniformVec2("translation");

	public FontShader() {
		super(new Shader(VERTEX_FILE, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_FILE, GL20.GL_FRAGMENT_SHADER));
	}
}