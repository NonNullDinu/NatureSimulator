package ns.shaders;

import org.lwjgl.opengl.GL20;

public class MenuDNAShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "shaders/menuDNA/vertexShader.glsl";
	private static final String FRAGMENT_SHADER = "shaders/menuDNA/fragmentShader.glsl";

	public UniformMat4 projectionMatrix = locator.locateUniformMat4("projectionMatrix");
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");
	
	public MenuDNAShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
}