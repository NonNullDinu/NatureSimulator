package ns.shaders;

import org.lwjgl.opengl.GL20;

public class MovingEntitySpotShader extends ShaderProgram {
	private static final String VERTEX_SHADER = "movingEntity/movingEntitySpotVertex.glsl";
	private static final String FRAGMENT_SHADER = "movingEntity/movingEntitySpotFragment.glsl";
	public UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");
	public UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public UniformFloat[] y = locator.getArrayLocation("y");
//	private UniformFloat[] y = new UniformFloat[] {
//			new UniformFloat("y[0]"),
//			new UniformFloat("y[1]"),
//			new UniformFloat("y[2]"),
//			new UniformFloat("y[3]"),
//			new UniformFloat("y[4]"),
//			new UniformFloat("y[5]"),
//			new UniformFloat("y[6]"),
//			new UniformFloat("y[7]"),
//			new UniformFloat("y[8]"),
//	};

	public MovingEntitySpotShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
}