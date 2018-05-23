package ns.shaders;

public class GUIShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/guis/guiVertex.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/guis/guiFragment.glsl";
	
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");

	public GUIShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

}
