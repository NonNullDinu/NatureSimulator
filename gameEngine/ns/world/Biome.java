package ns.world;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import ns.rivers.RiverEnd;
import ns.terrain.Terrain;

public enum Biome {

	FOREST(1, new Vector3f(0.02f, 0.678f, 0.22f), null),
	SWAMP(2, new Vector3f(0.659f, 0.569f, 0.62f), new BiomePreconditions() {
		@Override
		public boolean accept(Terrain terrain, Vector3f point) {
			List<RiverEnd> riverEnds = terrain.getRiverEnds();
			for(RiverEnd riverEnd : riverEnds) {
				if(Vector3f.sub(point, riverEnd.getPosition(), null).length() < 150f)
					return true;
			}
			return false;
		}
	}), SNOW_LANDS(3, new Vector3f(0.9f, 0.9f, 0.9f), null),;

	protected int biomeId;
	protected Vector3f color;
	private BiomePreconditions conditions;

	private Biome(int biomeId, Vector3f color, BiomePreconditions conditions) {
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
		return (conditions == null ? true : conditions.accept(terrain, point));
	}

	public static Biome get(int id) {
		if (id == FOREST.biomeId)
			return FOREST;
		else if (id == SWAMP.biomeId)
			return SWAMP;
		else if (id == SNOW_LANDS.biomeId)
			return SNOW_LANDS;
		return null;
	}

	private interface BiomePreconditions {
		public abstract boolean accept(Terrain terrain, Vector3f point);
	}
}