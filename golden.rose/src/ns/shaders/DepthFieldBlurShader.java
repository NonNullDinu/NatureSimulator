package ns.shaders;

import org.lwjgl.opengl.GL20;

public class DepthFieldBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "depthFieldBlur/vertex.glsl";
	private static final String FRAGMENT_SHADER = "depthFieldBlur/fragment.glsl";

	private final UniformSampler2D colorTexture = new UniformSampler2D("colorTexture");
	private final UniformSampler2D depthTexture = new UniformSampler2D("depthTexture");
	private final UniformSampler2D bluredTexture = new UniformSampler2D("bluredTexture");

	public final UniformVec2 nearFarPlanes = new UniformVec2("nearFarPlanes");

	public DepthFieldBlurShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(colorTexture, depthTexture, bluredTexture);
	}
	
	public void connectTextureUnits() {
		colorTexture.load(0);
		depthTexture.load(1);
		bluredTexture.load(2);
	}
}