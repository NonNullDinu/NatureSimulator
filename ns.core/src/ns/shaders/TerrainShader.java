package ns.shaders;

import ns.shaders.uniformStructs.UniformFogValues;
import ns.shaders.uniformStructs.UniformLight;
import ns.terrain.Terrain;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "terrain/terrainVertex.glsl";
	private static final String FRAGMENT_SHADER = "terrain/terrainFragment.glsl";

	public final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");
	public final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");

	public final UniformLight sun = new UniformLight("sun", locator);
	public final UniformLight moon = new UniformLight("moon", locator);

	public final UniformVec4 clipPlane = new UniformVec4("clipPlane");

	public final UniformVec3 skyColor = new UniformVec3("skyColor");

	public final UniformFogValues fogValues = new UniformFogValues("fogValues", locator);
	
	private int colors_buffer;
	
	public TerrainShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(projectionMatrix, transformationMatrix, viewMatrix, sun, moon, clipPlane, skyColor, fogValues);
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
