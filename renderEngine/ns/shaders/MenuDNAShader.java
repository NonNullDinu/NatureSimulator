package ns.shaders;

public class MenuDNAShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/menuDNA/vertexShader.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/menuDNA/fragmentShader.glsl";

	public UniformMat4 projectionMatrix = locator.locateUniformMat4("projectionMatrix");
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");
	
	public MenuDNAShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
}