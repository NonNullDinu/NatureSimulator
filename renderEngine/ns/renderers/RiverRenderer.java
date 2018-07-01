package ns.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import ns.camera.ICamera;
import ns.openglObjects.VAO;
import ns.rivers.River;
import ns.shaders.RiverShader;
import ns.utils.Maths;

public class RiverRenderer {
	private RiverShader shader;

	public RiverRenderer(Matrix4f projectionMatrix) {
		shader = new RiverShader();
		shader.start();
		shader.projectionMatrix.load(projectionMatrix);
		shader.stop();
	}

	public void render(List<River> rivers, ICamera camera) {
		shader.start();
		shader.viewMatrix.load(Maths.createViewMatrix(camera));
		shader.color.load(new Vector3f(0.6f, 0.9f, 0.9f));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		for (River river : rivers) {
			VAO model = river.getModel();
			model.bind(0, 1, 2);
			shader.loc.load(0);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, river.vao_length());
			shader.loc.load(1);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, river.vao_length());
			shader.loc.load(2);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, river.vao_length());
			model.unbind();
		}
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}