package ns.components;

import ns.openglWorkers.ModelsLibrary;
import ns.world.Biome;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BlueprintCreator {
	public static Blueprint createBlueprintFor(String entityFolder) {
		Blueprint blueprint = new Blueprint(entityFolder);
		if (entityFolder.equals("1000")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1000/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors))
					.withLife(new LifeComponent(MIN_TO_SECONDS(120)));
		} else if (entityFolder.equals("1001")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1001/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors))
					.withLife(new LifeComponent(MIN_TO_SECONDS(120)));
		} else if (entityFolder.equals("1002")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1002/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors))
					.withLife(new LifeComponent(MIN_TO_SECONDS(120)));
		} else if (entityFolder.equals("1003")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1003/mushroom.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.SWAMP))
					.withLife(new LifeComponent(MIN_TO_SECONDS(5)));
		} else if (entityFolder.equals("1004")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0.002494f, 0.350834f, 0.000000f));
			colors.add(new Vector3f(0.000000f, 0.307499f, 0.002174f));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1004/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.SNOW_LANDS))
					.withCuctomColors(new CustomColorsComponent(colors))
					.withLife(new LifeComponent(MIN_TO_SECONDS(120)));
		} else if (entityFolder.equals("1005")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0.002494f, 0.350834f, 0.000000f));
			colors.add(new Vector3f(0.000000f, 0.307499f, 0.002174f));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1005/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.SNOW_LANDS))
					.withCuctomColors(new CustomColorsComponent(colors))
					.withLife(new LifeComponent(MIN_TO_SECONDS(120)));
		} else if (entityFolder.equals("1006")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1006/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
					.withLife(new LifeComponent(MIN_TO_SECONDS(120)));
		} else if (entityFolder.equals("1007")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1007/grass.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(3, 30).withBiome(Biome.GRASS_LAND))
					.withLife(new LifeComponent(MIN_TO_SECONDS(10)));
		} else if (entityFolder.equals("1008")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1008/stone.mdl")));
		} else if (entityFolder.equals("1009")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1009/seaWeed.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(3, 30).withBiome(Biome.RIVER_LAND))
					.withLife(new LifeComponent(MIN_TO_SECONDS(30)));
		} else if (entityFolder.equals("1010")) {
			List<Vector3f> colors = new ArrayList<>();
			colors.add(new Vector3f(0, 1, 0));
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1010/tree.mdl")))
					.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(10, 60).withBiome(Biome.FOREST))
					.withCuctomColors(new CustomColorsComponent(colors))
					.withLife(new LifeComponent(MIN_TO_SECONDS(120)));
		} else if (entityFolder.equals("1011")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1011/snowman.mdl")))
					.withMovement(new MovementComponent(MovementComponent.MOVE))
					.withLife(new LifeComponent(MIN_TO_SECONDS(50)));
		} else if (entityFolder.equals("1012")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1012/sheep.mdl")))
					.withMovement(new MovementComponent(MovementComponent.MOVE | MovementComponent.JUMP))
					.withLife(new LifeComponent(MIN_TO_SECONDS(0.1f)));
		}
		return blueprint;
	}

	private static float MIN_TO_SECONDS(float i) {
		return i * 60f;
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
			blueprint.withModel(
					new ModelComponent(ModelsLibrary.getModel("models/1003/mushroom.mdl")).shouldScaleTrue());
		} else if (entityFolder.equals("menuDNA")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/others/menu_DNA.obj")));
		} else if (entityFolder.equals("1004")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1004/tree.mdl")));
		} else if (entityFolder.equals("1005")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1005/tree.mdl")));
		} else if (entityFolder.equals("1006")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1006/tree.mdl")));
		} else if (entityFolder.equals("1007")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1007/grass.mdl")).shouldScaleTrue());
		} else if (entityFolder.equals("1008")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1008/stone.mdl")).shouldScaleTrue());
		} else if (entityFolder.equals("1009")) {
			blueprint
					.withModel(new ModelComponent(ModelsLibrary.getModel("models/1009/seaWeed.mdl")).shouldScaleTrue());
		} else if (entityFolder.equals("1010")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1010/tree.mdl")));
		} else if (entityFolder.equals("1011")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1011/snowman.mdl")));
		} else if (entityFolder.equals("1012")) {
			blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1012/sheep.mdl")).shouldScaleTrue());
		}
		return blueprint;
	}
}