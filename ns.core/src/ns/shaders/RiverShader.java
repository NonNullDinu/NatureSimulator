package ns.shaders;

import ns.shaders.uniformStructs.UniformFogValues;
import org.lwjgl.opengl.GL20;

public class RiverShader extends ShaderProgram {
	public UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");
	public UniformVec3 color = new UniformVec3("color");
	public UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public UniformInt loc = new UniformInt("loc");
	public UniformVec3 skyColor = new UniformVec3("skyColor");
	public UniformFogValues fogValues = new UniformFogValues("fogValues", locator);

	public RiverShader() {
		super(new Shader("river/riverVertex.glsl", GL20.GL_VERTEX_SHADER),
				new Shader("river/riverFragment.glsl", GL20.GL_FRAGMENT_SHADER));
		storeUniforms(transformationMatrix, color, projectionMatrix, viewMatrix, loc, skyColor, fogValues);
	}
}