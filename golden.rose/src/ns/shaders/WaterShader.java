package ns.shaders;

import ns.shaders.uniformStructs.UniformFogValues;
import ns.shaders.uniformStructs.UniformLight;
import org.lwjgl.opengl.GL20;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "water/vertex.glsl";
	private static final String FRAGMENT_SHADER = "water/fragment.glsl";

	public final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public final UniformFloat waveTime = new UniformFloat("waveTime");
	public final UniformFloat one = new UniformFloat("one");
	public final UniformVec3 cameraPosition = new UniformVec3("cameraPos");
	public final UniformVec2 nearFarPlanes = new UniformVec2("nearFarPlanes");
	public final UniformVec3 skyColor = new UniformVec3("skyColor");
	public final UniformLight sun = new UniformLight("sun", locator);
	public final UniformLight moon = new UniformLight("moon", locator);
	public final UniformFogValues fogValues = new UniformFogValues("fogValues", locator);
	private final UniformSampler2D reflectionTexture = new UniformSampler2D("reflectionTexture");
	//	private final UniformSampler2D reflectionBluredTexture = new UniformSampler2D("reflectionBlured");
	private final UniformSampler2D refractionTexture = new UniformSampler2D("refractionTexture");
	//	private final UniformSampler2D refractionBluredTexture = new UniformSampler2D("refractionBlured");
	private final UniformSampler2D depthTexture = new UniformSampler2D("depthTexture");

	public WaterShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(projectionMatrix, viewMatrix, waveTime, one, reflectionTexture,
				refractionTexture, depthTexture,
				cameraPosition, nearFarPlanes, skyColor, sun, moon, fogValues);
	}

	public void connectTextureUnits() {
		reflectionTexture.load(0);
		refractionTexture.load(1);
		depthTexture.load(2);
//		reflectionBluredTexture.load(3);
//		refractionBluredTexture.load(4);
	}
}