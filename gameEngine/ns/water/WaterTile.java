package ns.water;

import java.util.Arrays;

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

	public WaterTile(float x, float y, Terrain t) {
		this.x = x;
		this.y = y;
		model = createModel(t);
		tile = this;
	}

	private VAO createModel(Terrain t) {
		float[] vao_pos = new float[(VERTEX_COUNT - 1) * (VERTEX_COUNT - 1) * 12];
		byte[] vao_indicators = new byte[vao_pos.length * 2];
		Arrays.fill(vao_pos, -TILE_SIZE - 1f);
		Arrays.fill(vao_indicators, (byte) -2);
		int quadPointer = 0;
		for (int z = 0; z < VERTEX_COUNT - 1; z++) {
			for (int x = 0; x < VERTEX_COUNT - 1; x++) {
				float xm = ((float) x / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				float zm = ((float) z / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				float xM = (((float) x + 1) / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				float zM = (((float) z + 1) / ((float) VERTEX_COUNT - 1) - 0.5f) * 2 * TILE_SIZE;
				if (t.getHeight(xm, zm) < 2f || t.getHeight(xm, zM) < 2f || t.getHeight(xM, zm) < 2f
						|| t.getHeight(xM, zM) < 2f) {
					vao_pos[quadPointer * 12] = xm;
					vao_pos[quadPointer * 12 + 1] = zm;

					vao_pos[quadPointer * 12 + 2] = xm;
					vao_pos[quadPointer * 12 + 3] = zM;

					vao_pos[quadPointer * 12 + 4] = xM;
					vao_pos[quadPointer * 12 + 5] = zm;

					vao_pos[quadPointer * 12 + 6] = xM;
					vao_pos[quadPointer * 12 + 7] = zm;

					vao_pos[quadPointer * 12 + 8] = xm;
					vao_pos[quadPointer * 12 + 9] = zM;

					vao_pos[quadPointer * 12 + 10] = xM;
					vao_pos[quadPointer * 12 + 11] = zM;

					vao_indicators[quadPointer * 24] = 0;
					vao_indicators[quadPointer * 24 + 1] = 1;
					vao_indicators[quadPointer * 24 + 2] = 1;
					vao_indicators[quadPointer * 24 + 3] = 0;

					vao_indicators[quadPointer * 24 + 4] = 0;
					vao_indicators[quadPointer * 24 + 5] = -1;
					vao_indicators[quadPointer * 24 + 6] = 1;
					vao_indicators[quadPointer * 24 + 7] = -1;

					vao_indicators[quadPointer * 24 + 8] = -1;
					vao_indicators[quadPointer * 24 + 9] = 0;
					vao_indicators[quadPointer * 24 + 10] = -1;
					vao_indicators[quadPointer * 24 + 11] = 1;

					vao_indicators[quadPointer * 24 + 12] = -1;
					vao_indicators[quadPointer * 24 + 13] = 1;
					vao_indicators[quadPointer * 24 + 14] = 0;
					vao_indicators[quadPointer * 24 + 15] = 1;

					vao_indicators[quadPointer * 24 + 16] = 1;
					vao_indicators[quadPointer * 24 + 17] = -1;
					vao_indicators[quadPointer * 24 + 18] = 1;
					vao_indicators[quadPointer * 24 + 19] = 0;

					vao_indicators[quadPointer * 24 + 20] = 0;
					vao_indicators[quadPointer * 24 + 21] = -1;
					vao_indicators[quadPointer * 24 + 22] = -1;
					vao_indicators[quadPointer * 24 + 23] = 0;

					quadPointer++;
				}
			}
		}
		vao_pos = remUnused(vao_pos);
		vao_indicators = remUnused(vao_indicators);
		return VAOLoader.storeDataInVAO(new VBOData(vao_pos).withAttributeNumber(0).withDimensions(2),
				new VBOData(vao_indicators).withAttributeNumber(1).withDimensions(4));
	}

	private byte[] remUnused(byte[] arr) {
		int sz = 0;
		for(byte b : arr)
			if(b != -2)
				sz++;
		byte[] result = new byte[sz];
		int ptr = 0;
		for(int i = 0; i < arr.length; i++)
			if(arr[i] != -2)
				result[ptr++] = arr[i];
		return result;
	}

	private float[] remUnused(float[] arr) {
		int sz = 0;
		for(float f : arr)
			if(f != -TILE_SIZE - 1f)
				sz++;
		float[] result = new float[sz];
		int ptr = 0;
		for(int i = 0; i < arr.length; i++)
			if(arr[i] != -TILE_SIZE - 1f)
				result[ptr++] = arr[i];
		return result;
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