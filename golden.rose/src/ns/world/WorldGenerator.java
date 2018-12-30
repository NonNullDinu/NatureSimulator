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

package ns.world;

import ns.components.Blueprint;
import ns.components.BlueprintCreator;
import ns.entities.Entity;
import ns.terrain.Terrain;
import ns.utils.GU;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenerator {
	private static final float TS = Terrain.SIZE / 2f;
	public static World generatedWorld = null;

	public static World generateWorld() {
//		In resource = SaveData.getResourceAt("save0." + GU.WORLD_SAVE_FILE_FORMAT, true);
//		if (resource.exists()) {
//			generatedWorld = LoadWorldMaster.loadWorld(resource);
//			generatedWorld.getTerrain().initColors(generatedWorld.getEntities());
//			if(generatedWorld.getRivers() != null)
//				GU.sendRequestToMainThread(new SetRequest(generatedWorld.getRivers()));
//		}
//		else {
		Terrain terrain = new Terrain();
		List<Entity> entities = createEntities(terrain);
		terrain.initColors(entities);
		generatedWorld = new World(entities, terrain);
//		}
//		if (generatedWorld == null || generatedWorld.getEntities() == null || generatedWorld.getTerrain() == null) {
//			System.out.println("Something went wrong while loading world, generating new");
//			Terrain terrain = new Terrain();
//			List<Entity> entities = createEntities(terrain);
//			terrain.initColors(entities);
//			generatedWorld = new World(entities, terrain);
//		}
		return generatedWorld;
	}

	private static List<Entity> createEntities(Terrain terrain) {
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 1500; i++) {
			float x = random.nextFloat() * 2f - 1f;
			float z = random.nextFloat() * 2f - 1f;
			x *= TS;
			z *= TS;
//			x += x < 0 ? 50f : -50f;
//			z += z < 0 ? 50f : -50f;
			float y = terrain.getHeight(x, z);
			Vector3f pos = new Vector3f(x, y, z);
			int type = GU.random.genInt(GU.TOTAL_NUMBER_OF_ENTITIES);
			Blueprint b = BlueprintCreator.createBlueprintFor(Integer.toString(1000 + type));
			entities.add(new Entity(b, pos));
		}
		return entities;
	}
}