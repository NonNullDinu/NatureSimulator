package ns.shaders;

public class DepthFieldBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/depthFieldBlur/vertex.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/depthFieldBlur/fragment.glsl";
	
	private UniformSampler2D colorTexture = locator.locateUniformSampler2D("colorTexture");
	private UniformSampler2D depthTexture = locator.locateUniformSampler2D("depthTexture");
	private UniformSampler2D bluredTexture = locator.locateUniformSampler2D("bluredTexture");
	
	public UniformVec2 nearFarPlanes = locator.locateUniformVec2("nearFarPlanes");

	public DepthFieldBlurShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
	
	public void connectTextureUnits() {
		colorTexture.load(0);
		depthTexture.load(1);
		bluredTexture.load(2);
	}
}