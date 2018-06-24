package ns.shaders;

import org.lwjgl.opengl.GL20;

import ns.shaders.uniformStructs.UniformFogValues;
import ns.shaders.uniformStructs.UniformLight;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "shaders/standard/vertexShader.glsl";
	private static final String FRAGMENT_SHADER = "shaders/standard/fragmentShader.glsl";

	public UniformMat4 projectionMatrix = locator.locateUniformMat4("projectionMatrix");
	public UniformMat4 viewMatrix = locator.locateUniformMat4("viewMatrix");
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");

	public UniformLight light = new UniformLight("light", locator);

	public UniformVec4 clipPlane = locator.locateUniformVec4("clipPlane");
	public UniformVec3[] customColors = { locator.locateUniformVec3("customColors[0]", false),
			locator.locateUniformVec3("customColors[1]", false), locator.locateUniformVec3("customColors[2]", false) };
	
	public UniformVec3 skyColor = locator.locateUniformVec3("skyColor");

	public UniformFogValues fogValues = new UniformFogValues("fogValues", locator);
	
	public UniformFloat time = locator.locateUniformFloat("time");
	
	public StaticShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
}
