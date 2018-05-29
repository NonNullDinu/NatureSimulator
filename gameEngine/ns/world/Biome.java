package ns.world;

import org.lwjgl.util.vector.Vector3f;

public enum Biome {

	FOREST(1, new Vector3f(0.02f, 0.678f, 0.22f)), SWAMP(2, new Vector3f(0.659f, 0.569f, 0.62f)),;

	protected int biomeId;
	protected Vector3f color;

	private Biome(int biomeId, Vector3f color) {
		this.biomeId = biomeId;
		this.color = color;
	}

	public int getId() {
		return biomeId;
	}

	public Vector3f getColor() {
		return color;
	}

	public static Biome get(int id) {
		if (id == FOREST.biomeId)
			return FOREST;
		else if (id == SWAMP.biomeId)
			return SWAMP;
		return null;
	}
}