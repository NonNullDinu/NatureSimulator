/*
 * Copyright (C) 2018  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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