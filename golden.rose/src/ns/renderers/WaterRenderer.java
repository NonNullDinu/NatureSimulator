package ns.renderers;

import ns.camera.ICamera;
import ns.display.DisplayManager;
import ns.entities.Light;
import ns.openglObjects.VAO;
import ns.shaders.WaterShader;
import ns.water.WaterFBOs;
import ns.water.WaterTile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class WaterRenderer {
	private static final float WAVE_SPEED = 0.125f;

	private final WaterShader shader;
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

	public void render(WaterTile water, ICamera camera, WaterFBOs fbos, Light sun, Light moon) {
		shader.start();

		waveTime += WAVE_SPEED * DisplayManager.getInGameTimeSeconds();
		shader.waveTime.load(waveTime);

		shader.skyColor.load(MasterRenderer.CLEAR_COLOR);
		shader.fogValues.load(MasterRenderer.FOG_VALUES);

		shader.sun.load(sun);
		shader.moon.load(moon);

		shader.viewMatrix.load(camera.getViewMatrix());
		shader.cameraPosition.load(camera.getPosition());

		fbos.getReflection().getTex().bindToTextureUnit(0);
		fbos.getRefraction().getTex().bindToTextureUnit(1);
		fbos.getRefraction().getDepthTex().bindToTextureUnit(2);
//		fbos.getBluredReflection().getTex().bindToTextureUnit(3);
//		fbos.getBluredRefraction().getTex().bindToTextureUnit(4);

		VAO model = water.getModel();
		model.bind(0, 1);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		model.unbind();

		shader.stop();
	}
}