package ns.components;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import ns.openglWorkers.ModelsLibrary;
import ns.world.Biome;

public class BlueprintCreator {
	public static Blueprint createBlueprintFor(String entityFolder) {
		Blueprint blueprint = new Blueprint(entityFolder);
		if (entityFolder.equals("1000")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/1000/tree.obj")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(10, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors));
		} else if (entityFolder.equals("1001")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/1001/tree.obj")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(10, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors));
		} else if (entityFolder.equals("1002")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/1002/tree.obj")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(10, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors));
		} else if (entityFolder.equals("1003")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/1003/mushroom.obj")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(10, 60).withBiome(Biome.SWAMP));
		}
		return blueprint;
	}

	public static Blueprint createModelBlueprintFor(String entityFolder) {
		Blueprint blueprint = new Blueprint(entityFolder);
		if (entityFolder.equals("1000")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/1000/tree.obj")));
		} else if (entityFolder.equals("1001")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/1001/tree.obj")));
		} else if (entityFolder.equals("1002")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/1002/tree.obj")));
		} else if (entityFolder.equals("1003")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/1003/mushroom.obj")));
		} else if (entityFolder.equals("menuDNA")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/others/menu_DNA.obj")));
		}
		return blueprint;
	}
}