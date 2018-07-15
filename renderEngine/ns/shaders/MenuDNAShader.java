package ns.shaders;

import org.lwjgl.opengl.GL20;

public class MenuDNAShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "menuDNA/vertexShader.glsl";
	private static final String FRAGMENT_SHADER = "menuDNA/fragmentShader.glsl";

	public UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");

	public MenuDNAShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(projectionMatrix, transformationMatrix);
	}
}