package ns.shaders;

import org.lwjgl.opengl.GL20;

public class DepthFieldBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "shaders/depthFieldBlur/vertex.glsl";
	private static final String FRAGMENT_SHADER = "shaders/depthFieldBlur/fragment.glsl";
	
	private UniformSampler2D colorTexture = locator.locateUniformSampler2D("colorTexture");
	private UniformSampler2D depthTexture = locator.locateUniformSampler2D("depthTexture");
	private UniformSampler2D bluredTexture = locator.locateUniformSampler2D("bluredTexture");
	
	public UniformVec2 nearFarPlanes = locator.locateUniformVec2("nearFarPlanes");

	public DepthFieldBlurShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
	
	public void connectTextureUnits() {
		colorTexture.load(0);
		depthTexture.load(1);
		bluredTexture.load(2);
	}
}