package ns.shaders;

import ns.shaders.uniformStructs.UniformFogValues;
import ns.shaders.uniformStructs.UniformLight;
import org.lwjgl.opengl.GL20;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "standard/vertexShader.glsl";
	private static final String FRAGMENT_SHADER = "standard/fragmentShader.glsl";

	public final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");

	public final UniformLight sun = new UniformLight("sun", locator);
	public final UniformLight moon = new UniformLight("moon", locator);

	public final UniformVec4 clipPlane = new UniformVec4("clipPlane");
	//	public UniformVec3[] customColors = { new UniformVec3("customColors[0]"), new UniformVec3("customColors[1]"),
//			new UniformVec3("customColors[2]") };
	public final UniformVec3[] customColors = locator.getArrayLocation("customColors");

	public final UniformVec3 skyColor = new UniformVec3("skyColor");

	public final UniformFogValues fogValues = new UniformFogValues("fogValues", locator);

	public final UniformFloat time = new UniformFloat("time");
	public final UniformFloat alpha = new UniformFloat("alpha");

	public final UniformFloat movementStopHeight = new UniformFloat("stopMovementHeight");

	public StaticShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(projectionMatrix, viewMatrix, transformationMatrix, sun, moon, clipPlane, customColors[0],
				customColors[1], customColors[2], skyColor, fogValues, time, alpha, movementStopHeight);
	}
}
