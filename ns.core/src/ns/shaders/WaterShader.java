package ns.shaders;

import ns.shaders.uniformStructs.UniformFogValues;
import ns.shaders.uniformStructs.UniformLight;
import org.lwjgl.opengl.GL20;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "water/vertex.glsl";
	private static final String FRAGMENT_SHADER = "water/fragment.glsl";

	public UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public UniformFloat waveTime = new UniformFloat("waveTime");
	public UniformFloat one = new UniformFloat("one");

	private UniformSampler2D reflectionTexture = new UniformSampler2D("reflectionTexture");
	private UniformSampler2D refractionTexture = new UniformSampler2D("refractionTexture");
	private UniformSampler2D depthTexture = new UniformSampler2D("depthTexture");

	public UniformVec3 cameraPosition = new UniformVec3("cameraPos");
	public UniformVec2 nearFarPlanes = new UniformVec2("nearFarPlanes");
	public UniformVec3 skyColor = new UniformVec3("skyColor");

	public UniformLight light = new UniformLight("light", locator);

	public UniformFogValues fogValues = new UniformFogValues("fogValues", locator);

	public WaterShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(projectionMatrix, viewMatrix, waveTime, one, reflectionTexture, refractionTexture, depthTexture,
				cameraPosition, nearFarPlanes, skyColor, light, fogValues);
	}

	public void connectTextureUnits() {
		reflectionTexture.load(0);
		refractionTexture.load(1);
		depthTexture.load(2);
	}
}