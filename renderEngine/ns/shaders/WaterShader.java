package ns.shaders;

import ns.shaders.uniformStructs.UniformFogValues;
import ns.shaders.uniformStructs.UniformLight;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/water/vertex.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/water/fragment.glsl";

	public UniformMat4 projectionMatrix = locator.locateUniformMat4("projectionMatrix");
	public UniformMat4 viewMatrix = locator.locateUniformMat4("viewMatrix");
	public UniformFloat waveTime = locator.locateUniformFloat("waveTime");
	public UniformFloat one = locator.locateUniformFloat("one");

	private UniformSampler2D reflectionTexture = locator.locateUniformSampler2D("reflectionTexture");
	private UniformSampler2D refractionTexture = locator.locateUniformSampler2D("refractionTexture");
	private UniformSampler2D depthTexture = locator.locateUniformSampler2D("depthTexture");
	
	public UniformVec3 cameraPosition = locator.locateUniformVec3("cameraPos");
	public UniformVec2 nearFarPlanes = locator.locateUniformVec2("nearFarPlanes");
	public UniformVec3 skyColor = locator.locateUniformVec3("skyColor");

	public UniformLight light = new UniformLight("light", locator);
	
	public UniformFogValues fogValues = new UniformFogValues("fogValues", locator);
	
	public WaterShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	public void connectTextureUnits() {
		reflectionTexture.load(0);
		refractionTexture.load(1);
		depthTexture.load(2);
	}
}