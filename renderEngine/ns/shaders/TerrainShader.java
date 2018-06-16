package ns.shaders;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import ns.shaders.uniformStructs.UniformFogValues;
import ns.shaders.uniformStructs.UniformLight;
import ns.terrain.Terrain;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "res/shaders/terrain/terrainVertex.glsl";
	private static final String FRAGMENT_SHADER = "res/shaders/terrain/terrainFragment.glsl";
	
	public UniformMat4 projectionMatrix = locator.locateUniformMat4("projectionMatrix");
	public UniformMat4 transformationMatrix = locator.locateUniformMat4("transformationMatrix");
	public UniformMat4 viewMatrix = locator.locateUniformMat4("viewMatrix");
	
	public UniformLight light = new UniformLight("light", locator);
	
	public UniformVec4 clipPlane = locator.locateUniformVec4("clipPlane");
	
	public UniformVec3 skyColor = locator.locateUniformVec3("skyColor");

	public UniformFogValues fogValues = new UniformFogValues("fogValues", locator);
	
	private int colors_buffer;
	
	public TerrainShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
	
	@Override
	protected void postLink() {
		colors_buffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, colors_buffer);
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, Terrain.VERTEX_COUNT * Terrain.VERTEX_COUNT * 4, GL15.GL_STATIC_DRAW);
		GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 0, colors_buffer);
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
	}
	
	@Override
	public void cleanUp() {
		super.cleanUp();
		GL15.glDeleteBuffers(colors_buffer);
	}
}
