package ns.renderers;

import ns.camera.ICamera;
import ns.openglObjects.VAO;
import ns.rivers.River;
import ns.shaders.RiverShader;
import ns.utils.GU;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class RiverRenderer {
	private final RiverShader shader;

	public RiverRenderer(Matrix4f projectionMatrix) {
		shader = new RiverShader();
		shader.start();
		shader.projectionMatrix.load(projectionMatrix);
		shader.stop();
	}

	public void render(List<River> rivers, ICamera camera) {
		shader.start();
		shader.viewMatrix.load(camera.getViewMatrix());
		shader.color.load(new Vector3f(0.6f, 0.9f, 0.9f));
		shader.skyColor.load(MasterRenderer.CLEAR_COLOR);
		shader.fogValues.load(MasterRenderer.FOG_VALUES);
		GL11.glEnable(GL11.GL_BLEND);
		int e;
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		for (River river : rivers) {
			VAO model = river.getModel();
			model.bind(0, 1, 2);
			while ((e = GL11.glGetError()) != GL11.GL_NO_ERROR) {
				System.out.println("Errore1 " + e + " " + GU.getGLErrorType(e));
			}
			shader.loc.load(0);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, river.vao_length());
			while ((e = GL11.glGetError()) != GL11.GL_NO_ERROR) {
				System.out.println("Errore2 " + e + " " + GU.getGLErrorType(e));
			}
			shader.loc.load(1);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, river.vao_length());
			while ((e = GL11.glGetError()) != GL11.GL_NO_ERROR) {
				System.out.println("Errore3 " + e + " " + GU.getGLErrorType(e));
			}
			shader.loc.load(2);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, river.vao_length());
			while ((e = GL11.glGetError()) != GL11.GL_NO_ERROR) {
				System.out.println("Errore4 " + e + " " + GU.getGLErrorType(e));
			}
			model.unbind();
		}
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}