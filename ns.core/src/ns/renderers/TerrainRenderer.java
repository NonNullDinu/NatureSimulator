package ns.renderers;

import ns.openglObjects.VAO;
import ns.shaders.TerrainShader;
import ns.terrain.Terrain;
import ns.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class TerrainRenderer {
	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		this.shader.projectionMatrix.load(projectionMatrix);
		shader.stop();
	}
	
	public void render(Terrain terrain) {
		VAO vao = terrain.getModel();
		vao.bind(0, 1, 2);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1));
		GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		vao.unbind();
	}
}
