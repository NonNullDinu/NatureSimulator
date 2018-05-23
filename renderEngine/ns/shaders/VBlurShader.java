package ns.shaders;

public class VBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/blur/vvshader.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/blur/fshader.glsl";

	public UniformVec2 size = locator.locateUniformVec2("size");
	
	public VBlurShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
}