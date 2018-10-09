package ns.shaders;

import org.lwjgl.opengl.GL20;

public class HBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "blur/vhshader.glsl";
	private static final String FRAGMENT_SHADER = "blur/fshader.glsl";

	public final UniformVec2 size = new UniformVec2("size");

	public HBlurShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(size);
	}
}