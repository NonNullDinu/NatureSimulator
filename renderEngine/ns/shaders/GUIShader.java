package ns.shaders;

import org.lwjgl.opengl.GL20;

public class GUIShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "shaders/guis/guiVertex.glsl";
	private static final String FRAGMENT_SHADER = "shaders/guis/guiFragment.glsl";
	
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");

	public GUIShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}

}
