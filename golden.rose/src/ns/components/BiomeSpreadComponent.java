package ns.components;

import ns.world.Biome;
import org.lwjgl.util.vector.Vector2f;

public class BiomeSpreadComponent implements IComponent {
	private static final long serialVersionUID = 1297469013765161102L;

	private float minRange, maxRange;
	private Biome biome;
	private boolean addedColorsToTerrain = false;

	BiomeSpreadComponent withMinMaxRange(float minRange, float maxRange) {
		this.minRange = minRange;
		this.maxRange = maxRange;
		return this;
	}

	BiomeSpreadComponent withBiome(Biome biome) {
		this.biome = biome;
		return this;
	}

	public Vector2f getMinMax() {
		return new Vector2f(minRange, maxRange);
	}

	public Biome getBiome() {
		return biome;
	}

	public void setAddedColorsToTerrain(boolean addedColorsToTerrain) {
		this.addedColorsToTerrain = addedColorsToTerrain;
	}

	public boolean addedColorsToTerrain() {
		return addedColorsToTerrain;
	}

	@Override
	public IComponent copy() {
		return this;
	}
}