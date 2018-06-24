package ns.shaders;

import org.lwjgl.opengl.GL20;

public class VBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "shaders/blur/vvshader.glsl";
	private static final String FRAGMENT_SHADER = "shaders/blur/fshader.glsl";

	public UniformVec2 size = locator.locateUniformVec2("size");
	
	public VBlurShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
}