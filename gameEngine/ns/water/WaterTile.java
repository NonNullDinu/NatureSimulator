package ns.water;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.terrain.Terrain;

public class WaterTile {
	public static WaterTile tile;
	
	private static final float TILE_SIZE = Terrain.SIZE / 2f;
	private static final int VERTEX_COUNT = (int) (128f * (TILE_SIZE / 600f));

	private float x, y, height;
	private VAO model;

	public WaterTile(float x, float y) {
		this.x = x;
		this.y = y;
		model = createModel();
		tile = this;
	}

	private VAO createModel() {
		float[] vao_pos = new float[(VERTEX_COUNT - 1) * (VERTEX_COUNT - 1) * 12];
		byte[] vao_indicators = new byte[vao_pos.length * 2];
		int vertexPointer = 0;
		for (int z = 0; z < VERTEX_COUNT - 1; z++) {
			for (int x = 0; x < VERTEX_COUNT - 1; x++) {
				vao_pos[vertexPointer * 12] = ((float) x / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				vao_pos[vertexPointer * 12 + 1] = ((float) z / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;

				vao_pos[vertexPointer * 12 + 2] = ((float) x / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				vao_pos[vertexPointer * 12 + 3] = (((float) z + 1) / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;

				vao_pos[vertexPointer * 12 + 4] = (((float) x + 1) / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				vao_pos[vertexPointer * 12 + 5] = ((float) z / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;

				vao_pos[vertexPointer * 12 + 6] = (((float) x + 1) / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				vao_pos[vertexPointer * 12 + 7] = ((float) z / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;

				vao_pos[vertexPointer * 12 + 8] = ((float) x / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				vao_pos[vertexPointer * 12 + 9] = (((float) z + 1) / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;

				vao_pos[vertexPointer * 12 + 10] = (((float) x + 1) / ((float) VERTEX_COUNT - 1) - 0.5f) * 2
						* TILE_SIZE;
				vao_pos[vertexPointer * 12 + 11] = (((float) z + 1) / ((float) VERTEX_COUNT - 1) - 0.5f) * 2
						* TILE_SIZE;

				vao_indicators[vertexPointer * 24] = 0;
				vao_indicators[vertexPointer * 24 + 1] = 1;
				vao_indicators[vertexPointer * 24 + 2] = 1;
				vao_indicators[vertexPointer * 24 + 3] = 0;

				vao_indicators[vertexPointer * 24 + 4] = 0;
				vao_indicators[vertexPointer * 24 + 5] = -1;
				vao_indicators[vertexPointer * 24 + 6] = 1;
				vao_indicators[vertexPointer * 24 + 7] = -1;

				vao_indicators[vertexPointer * 24 + 8] = -1;
				vao_indicators[vertexPointer * 24 + 9] = 0;
				vao_indicators[vertexPointer * 24 + 10] = -1;
				vao_indicators[vertexPointer * 24 + 11] = 1;

				vao_indicators[vertexPointer * 24 + 12] = -1;
				vao_indicators[vertexPointer * 24 + 13] = 1;
				vao_indicators[vertexPointer * 24 + 14] = 0;
				vao_indicators[vertexPointer * 24 + 15] = 1;

				vao_indicators[vertexPointer * 24 + 16] = 1;
				vao_indicators[vertexPointer * 24 + 17] = -1;
				vao_indicators[vertexPointer * 24 + 18] = 1;
				vao_indicators[vertexPointer * 24 + 19] = 0;

				vao_indicators[vertexPointer * 24 + 20] = 0;
				vao_indicators[vertexPointer * 24 + 21] = -1;
				vao_indicators[vertexPointer * 24 + 22] = -1;
				vao_indicators[vertexPointer * 24 + 23] = 0;

				vertexPointer++;
			}
		}
		return VAOLoader.storeDataInVAO(new VBOData(vao_pos).withAttributeNumber(0).withDimensions(2),
				new VBOData(vao_indicators).withAttributeNumber(1).withDimensions(4));
	}

	public static float getTileSize() {
		return TILE_SIZE;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getHeight() {
		return height;
	}

	public VAO getModel() {
		return model;
	}

	public static int getVertexCount() {
		return VERTEX_COUNT;
	}
}