package ns.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.camera.ICamera;
import ns.entities.Light;
import ns.openglObjects.VAO;
import ns.shaders.WaterShader;
import ns.utils.Maths;
import ns.water.WaterFBOs;
import ns.water.WaterTile;

public class WaterRenderer {
	private static final float WAVE_SPEED = 0.003f;

	private WaterShader shader;
	private float waveTime;

	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.projectionMatrix.load(projectionMatrix);
		shader.connectTextureUnits();
		shader.one.load(WaterTile.getTileSize() / (float) WaterTile.getVertexCount());
		shader.nearFarPlanes.load(new Vector2f(0.1f, MasterRenderer.FAR_PLANE));
		shader.stop();
	}

	public void render(WaterTile water, ICamera camera, WaterFBOs fbos, Light sun) {
		shader.start();

		waveTime += WAVE_SPEED;
		shader.waveTime.load(waveTime);

		shader.skyColor.load(new Vector3f(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE));
		shader.fogValues.load(MasterRenderer.FOG_VALUES);

		shader.light.load(sun);

		shader.viewMatrix.load(Maths.createViewMatrix(camera));
		shader.cameraPosition.load(camera.getPosition());

		fbos.getReflexion().getTex().bindToTextureUnit(0);
		fbos.getRefraction().getTex().bindToTextureUnit(1);
		fbos.getRefraction().getDepthTex().bindToTextureUnit(2);

		VAO model = water.getModel();
		model.bind();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		model.unbind();

		shader.stop();
	}
}