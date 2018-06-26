package ns.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import ns.components.Blueprint;
import ns.components.BlueprintCreator;
import ns.entities.Entity;
import ns.parallelComputing.SetRequest;
import ns.terrain.Terrain;
import ns.utils.GU;
import ns.worldSave.LoadWorldMaster;
import res.Resource;

public class WorldGenerator {
	public static World generatedWorld = null;
	private static final float TS = Terrain.SIZE / 2f;

	public static World generateWorld() {
		Resource resource = new Resource().withLocation("saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT)
				.withVersion(true).create();
		if (resource.exists()) {
			generatedWorld = LoadWorldMaster.loadWorld(resource);
			generatedWorld.getTerrain().initColors(generatedWorld.getEntities());
			if(generatedWorld.getRivers() != null)
				GU.sendRequestToMainThread(new SetRequest(generatedWorld.getRivers()));
		}
		else {
			System.out.println("Save not found, generating new");
			Terrain terrain = new Terrain();
			List<Entity> entities = createEntities(terrain);
			terrain.initColors(entities);
			generatedWorld = new World(entities, terrain);
		}
		if (generatedWorld == null || generatedWorld.getEntities() == null || generatedWorld.getTerrain() == null) {
			System.out.println("Something went wrong while loading world, generating new");
			Terrain terrain = new Terrain();
			List<Entity> entities = createEntities(terrain);
			terrain.initColors(entities);
			generatedWorld = new World(entities, terrain);
		}
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
			float y = terrain.getHeight(x, z);
			Vector3f pos = new Vector3f(x, y, z);
			int type = GU.random.genInt(GU.TOTAL_NUMBER_OF_ENTITIES);
			Blueprint b = BlueprintCreator.createBlueprintFor(Integer.toString(1000 + type));
			entities.add(new Entity(b, pos));
		}
		return entities;
	}
}