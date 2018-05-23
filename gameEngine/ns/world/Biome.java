package ns.world;

import org.lwjgl.util.vector.Vector3f;

public enum Biome {
	
	FOREST(1, new Vector3f(0.02f, 0.678f, 0.22f)),
	;
	
	private int biomeId;
	private Vector3f color;
	
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
}