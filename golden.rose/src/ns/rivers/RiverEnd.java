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

package ns.rivers;

import ns.components.BlueprintCreator;
import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.terrain.Terrain;
import ns.utils.GU;
import ns.world.World;
import org.lwjgl.util.vector.Vector3f;

import java.io.Serializable;

public class RiverEnd implements Serializable {
	private static final long serialVersionUID = -4775100940305038961L;

	private final Vector3f position;
	private float timeSinceLastPlantCreated;
	private int spawned;

	public RiverEnd(Vector3f position) {
		this.position = new Vector3f(position);
		timeSinceLastPlantCreated = 0f;
	}

	public void update(World world) {
		timeSinceLastPlantCreated += DisplayManager.getInGameTimeSeconds();
		if (timeSinceLastPlantCreated >= 10f && spawned < 20) {
			float x = position.x + GU.random.genFloat() * 200f - 100f;
			float z = position.z + GU.random.genFloat() * 200f - 100f;
			while (x <= -Terrain.SIZE / 2f + 50f || x >= Terrain.SIZE / 2f - 50f || z <= -Terrain.SIZE / 2f + 50f
					|| z >= Terrain.SIZE / 2f + -50f) {
				x = position.x + GU.random.genFloat() * 200f - 100f;
				z = position.z + GU.random.genFloat() * 200f - 100f;
			}
			float y = world.getTerrain().getHeight(x, z);
			world.add(new Entity(BlueprintCreator.createBlueprintFor("1003"), new Vector3f(x, y, z)));
			timeSinceLastPlantCreated = 0f;
			spawned++;
		}
	}

	public Vector3f getPosition() {
		return position;
	}
}