package ns.shaders;

public class HBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/blur/vhshader.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/blur/fshader.glsl";
	
	public UniformVec2 size = locator.locateUniformVec2("size");

	public HBlurShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
}