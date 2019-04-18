/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
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