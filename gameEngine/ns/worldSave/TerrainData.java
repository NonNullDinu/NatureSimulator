package ns.worldSave;

import ns.terrain.Terrain;

public class TerrainData extends Data {
	private static final long serialVersionUID = -7194655106968723919L;

	private int seed;

	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public Terrain asInstance() {
		return new Terrain(seed);
	}
}