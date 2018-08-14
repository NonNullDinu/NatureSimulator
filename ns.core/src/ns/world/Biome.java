package ns.world;

import ns.rivers.RiverEnd;
import ns.terrain.Terrain;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public enum Biome {

	FOREST(1, new Vector3f(0.02f, 0.678f, 0.22f), null),
	SWAMP(2, new Vector3f(0.659f, 0.569f, 0.62f), (Terrain terrain, Vector3f point) -> {
		List<RiverEnd> riverEnds = terrain.getRiverEnds();
		for (RiverEnd riverEnd : riverEnds) {
			if (Vector3f.sub(point, riverEnd.getPosition(), null).length() < 150f)
				return true;
		}
		return false;
	}), SNOW_LANDS(3, new Vector3f(0.9f, 0.9f, 0.9f), null), GRASS_LAND(4, new Vector3f(0f, 0.9f, 0f), null),
	RIVER_LAND(5, new Vector3f(0.722f, 0.639f, 0.102f), null),
	;

	protected int biomeId;
	protected Vector3f color;
	private BiomePreconditions conditions;

	Biome(int biomeId, Vector3f color, BiomePreconditions conditions) {
		this.biomeId = biomeId;
		this.color = color;
		this.conditions = conditions;
	}

	public int getId() {
		return biomeId;
	}

	public Vector3f getColor() {
		return color;
	}

	public boolean accept(Terrain terrain, Vector3f point) {
		return (conditions == null || conditions.accept(terrain, point));
	}

	public static Biome get(int id) {
		for (Biome b : Biome.values())
			if (id == b.biomeId)
				return b;
		return null;
	}

	private interface BiomePreconditions {
		boolean accept(Terrain terrain, Vector3f point);
	}
}