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
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1000/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors));
		} else if (entityFolder.equals("1001")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1001/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors));
		} else if (entityFolder.equals("1002")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1002/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors));
		} else if (entityFolder.equals("1003")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1003/mushroom.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.SWAMP));
		} else if (entityFolder.equals("1004")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0.002494f, 0.350834f, 0.000000f));
			colors.add(new Vector3f(0.000000f, 0.307499f, 0.002174f));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1004/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.SNOW_LANDS))
					.withCuctomColors(new CustomColorsComponent(colors));
		}
		return blueprint;
	}

	public static Blueprint createModelBlueprintFor(String entityFolder) {
		Blueprint blueprint = new Blueprint(entityFolder);
		if (entityFolder.equals("1000")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1000/tree.mdl")));
		} else if (entityFolder.equals("1001")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1001/tree.mdl")));
		} else if (entityFolder.equals("1002")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1002/tree.mdl")));
		} else if (entityFolder.equals("1003")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1003/mushroom.mdl")));
		} else if (entityFolder.equals("menuDNA")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/others/menu_DNA.obj")));
		} else if (entityFolder.equals("1004")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1004/tree.mdl")));
		}
		return blueprint;
	}
}