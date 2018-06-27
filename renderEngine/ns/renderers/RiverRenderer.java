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
	private VAO sphereVAO;

	public RiverRenderer(VAO sphereVAO, Matrix4f projectionMatrix) {
		this.sphereVAO = sphereVAO;
		shader = new RiverShader();
		shader.start();
		shader.projectionMatrix.load(projectionMatrix);
		shader.stop();
	}

	public void render(List<River> rivers, ICamera camera) {
		shader.start();
		shader.viewMatrix.load(Maths.createViewMatrix(camera));
		shader.color.load(new Vector3f(0.6f, 0.9f, 0.9f));
//		sphereVAO.bind(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		for (River river : rivers) {
//			for (WaterParticle particle : river.getParticles()) {
//				shader.transformationMatrix.load(
//						Maths.createTransformationMatrix(particle.getPosition(), 0, 0, 0, particle.getSize()));
//				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, sphereVAO.getVertexCount());
//			}
			VAO model = river.getModel();
			model.bind(0);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, river.getParticles().size() * 2);
			model.unbind();
		}
		GL11.glDisable(GL11.GL_BLEND);
//		sphereVAO.unbind();
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}