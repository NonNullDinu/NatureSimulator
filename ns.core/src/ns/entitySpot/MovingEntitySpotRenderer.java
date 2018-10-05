package ns.entitySpot;

import ns.camera.ICamera;
import ns.customFileFormat.TexFile;
import ns.entities.Entity;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.renderers.MasterRenderer;
import ns.shaders.MovingEntitySpotShader;
import ns.utils.Maths;
import ns.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MovingEntitySpotRenderer {
	private MovingEntitySpotShader shader;
	private MovingEntitySpot spot;

	public MovingEntitySpotRenderer(MovingEntitySpotShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.projectionMatrix.load(projectionMatrix);
		shader.stop();
		float[] positions = {-1f, -1f, // 0
				-1f, 0f, // 1
				-1f, 1f, // 2
				0f, -1f, // 3
				0f, 0f, // 4
				0f, 1f, // 5
				1f, -1f, // 6
				1f, 0f, // 7
				1f, 1f, // 8
		};
		int[] indices = {0, 1, 3, 3, 1, 4, // Back left
				3, 4, 6, 6, 4, 7, // Back right
				1, 2, 4, 4, 2, 5, // Front left
				4, 5, 7, 7, 5, 8 // Front right
		};
		this.spot = new MovingEntitySpot(new Vector3f(),
				VAOLoader.storeDataInVAO(new VBOData(positions).withDimensions(2).withAttributeNumber(0),
						new VBOData(indices).isIndices(true)),
				new TexFile("textures/movingEntityMark.tex").load(), new TexFile(
				"textures/movingEntityMarkNotPlaceable.tex").load());
	}

	public void renderAt(Entity entity, World world, ICamera camera) {
		Vector3f pos = entity.getPosition();
		spot.getPosition().set(new Vector3f(pos.x, pos.y - 4.5f, pos.z));
		Vector3f p = spot.getPosition();
		shader.start();
		shader.viewMatrix.load(Maths.createViewMatrix(camera));
		spot.getModel().bind(0);
		if ((entity).isHeightWithinLimits(spot.getPosition().y - 0.5f))
			spot.getPlaceableTexture().bindToTextureUnit(0);
		else
			spot.getNotPlaceableTexture().bindToTextureUnit(0);
		MasterRenderer.disableBackfaceCulling();
		final float scale = 2f;
		shader.transformationMatrix.load(Maths.createTransformationMatrix(spot.getPosition(), 0, 0, 0, scale));
		float[] ys = new float[9];
		ys[0] = world.getTerrain().getHeight(p.x - scale, p.z - scale) + 1f;
		ys[1] = world.getTerrain().getHeight(p.x - scale, p.z) + 1f;
		ys[2] = world.getTerrain().getHeight(p.x - scale, p.z + scale) + 1f;
		ys[3] = world.getTerrain().getHeight(p.x, p.z - scale) + 1f;
		ys[4] = world.getTerrain().getHeight(p.x, p.z) + 1f;
		ys[5] = world.getTerrain().getHeight(p.x, p.z + scale) + 1f;
		ys[6] = world.getTerrain().getHeight(p.x + scale, p.z - scale) + 1f;
		ys[7] = world.getTerrain().getHeight(p.x + scale, p.z) + 1f;
		ys[8] = world.getTerrain().getHeight(p.x + scale, p.z + scale) + 1f;
		for (int i = 0; i < 9; i++)
			shader.y[i].load(ys[i]);
		GL11.glDrawElements(GL11.GL_TRIANGLES, spot.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		MasterRenderer.enableBackfaceCulling();
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}