package ns.shaders;

import ns.shaders.uniformStructs.UniformFogValues;
import org.lwjgl.opengl.GL20;

public class RiverShader extends ShaderProgram {
	public final UniformVec3 color = new UniformVec3("color");
	public final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public final UniformInt loc = new UniformInt("loc");
	public final UniformVec3 skyColor = new UniformVec3("skyColor");
	public final UniformFogValues fogValues = new UniformFogValues("fogValues", locator);
	private final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");

	public RiverShader() {
		super(new Shader("river/riverVertex.glsl", GL20.GL_VERTEX_SHADER),
				new Shader("river/riverFragment.glsl", GL20.GL_FRAGMENT_SHADER));
		storeUniforms(transformationMatrix, color, projectionMatrix, viewMatrix, loc, skyColor, fogValues);
	}
}