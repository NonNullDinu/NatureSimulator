package ns.shaders;

public class ColorQuadFillShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/colorQuad/quadVertex.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/colorQuad/quadFragment.glsl";
	
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");
	public UniformVec3 color = locator.locateUniformVec3("color");

	public ColorQuadFillShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
}