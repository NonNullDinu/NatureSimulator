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

import ns.genetics.DNA;
import ns.openglWorkers.ModelsLibrary;
import ns.world.Biome;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BlueprintCreator {
	public static Blueprint createBlueprintFor(String entityFolder) {
		Blueprint blueprint = new Blueprint(entityFolder);
		switch (entityFolder) {
			case "1000": {
				List<Vector3f> colors = new ArrayList<>();
				colors.add(new Vector3f(0, 1, 0));
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1000/tree.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
						.withCustomColors(new CustomColorsComponent(colors))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(120)).withLimits(new Vector2f(0, 70)));
				break;
			}
			case "1001": {
				List<Vector3f> colors = new ArrayList<>();
				colors.add(new Vector3f(0, 1, 0));
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1001/tree.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
						.withCustomColors(new CustomColorsComponent(colors))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(120)).withLimits(new Vector2f(0, 70)));
				break;
			}
			case "1002": {
				List<Vector3f> colors = new ArrayList<>();
				colors.add(new Vector3f(0, 1, 0));
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1002/tree.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
						.withCustomColors(new CustomColorsComponent(colors))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(120)).withLimits(new Vector2f(0, 70)));
				break;
			}
			case "1003":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1003/mushroom.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.SWAMP))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(5)).withLimits(new Vector2f(0, 30)));
				break;
			case "1004": {
				List<Vector3f> colors = new ArrayList<>();
				colors.add(new Vector3f(0.002494f, 0.350834f, 0.000000f));
				colors.add(new Vector3f(0.000000f, 0.307499f, 0.002174f));
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1004/tree.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.SNOW_LANDS))
						.withCustomColors(new CustomColorsComponent(colors))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(120)).withLimits(new Vector2f(40, 120)));
				break;
			}
			case "1005": {
				List<Vector3f> colors = new ArrayList<>();
				colors.add(new Vector3f(0.002494f, 0.350834f, 0.000000f));
				colors.add(new Vector3f(0.000000f, 0.307499f, 0.002174f));
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1005/tree.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.SNOW_LANDS))
						.withCustomColors(new CustomColorsComponent(colors))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(120)).withLimits(new Vector2f(40, 120)));
				break;
			}
			case "1006":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1006/tree.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(5, 60).withBiome(Biome.FOREST))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(120)).withLimits(new Vector2f(0, 70)));
				break;
			case "1007":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1007/grass.mdl")).useHeightStopMovement(0.4f))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(3, 30).withBiome(Biome.GRASS_LAND))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(10)).withLimits(new Vector2f(0, 50)));
				break;
			case "1008":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1008/stone.mdl")));
				break;
			case "1009":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1009/seaWeed.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(3, 30).withBiome(Biome.RIVER_LAND))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(30)).withLimits(new Vector2f(-100, -10)));
				break;
			case "1010": {
				List<Vector3f> colors = new ArrayList<>();
				colors.add(new Vector3f(0, 1, 0));
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1010/tree.mdl")))
						.withBiomeSpread(new BiomeSpreadComponent().withMinMaxRange(10, 60).withBiome(Biome.FOREST))
						.withCustomColors(new CustomColorsComponent(colors))
						.withLife(new PlantLifeComponent(MIN_TO_SECONDS(120)).withLimits(new Vector2f(0, 70)));
				break;
			}
			case "1011":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1011/snowman.mdl")))
						.withMovement(new MovementComponent(MovementComponent.MOVE))
						.withLife(new AnimalLifeComponent(MIN_TO_SECONDS(50)).withDNA(DNA._default(1)).withReprTime(MIN_TO_SECONDS(5f)));
				break;
			case "1012":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1012/sheep.mdl")))
						.withMovement(new MovementComponent(MovementComponent.MOVE | MovementComponent.JUMP))
						.withLife(new AnimalLifeComponent(MIN_TO_SECONDS(2f)).withDNA(DNA._default(2)).withReprTime(MIN_TO_SECONDS(0.2f)));
				break;
			case "1013":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1013/meat.mdl"))).withFoodComponent(new FoodComponent(100f));
				break;
		}
		return blueprint;
	}

	private static float MIN_TO_SECONDS(float i) {
		return i * 60f;
	}

	public static Blueprint createModelBlueprintFor(String entityFolder) {
		Blueprint blueprint = new Blueprint(entityFolder);
		switch (entityFolder) {
			case "1000":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1000/tree.mdl")));
				break;
			case "1001":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1001/tree.mdl")));
				break;
			case "1002":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1002/tree.mdl")));
				break;
			case "1003":
				blueprint.withModel(
						new ModelComponent(ModelsLibrary.getModel("models/1003/mushroom.mdl")).shouldScaleTrue());
				break;
			case "1004":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1004/tree.mdl")));
				break;
			case "1005":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1005/tree.mdl")));
				break;
			case "1006":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1006/tree.mdl")));
				break;
			case "1007":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1007/grass.mdl")).shouldScaleTrue().useHeightStopMovement(0.4f));
				break;
			case "1008":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1008/stone.mdl")).shouldScaleTrue());
				break;
			case "1009":
				blueprint
						.withModel(new ModelComponent(ModelsLibrary.getModel("models/1009/seaWeed.mdl")).shouldScaleTrue());
				break;
			case "1010":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1010/tree.mdl")));
				break;
			case "1011":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1011/snowman.mdl")));
				break;
			case "1012":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1012/sheep.mdl")).shouldScaleTrue());
				break;
			case "1013":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/1013/meat.mdl")));
				break;
			case "menuDNA":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/others/menu_DNA.obj")));
				break;
			case "logo":
				blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("models/others/ingame-logo.obj")));
				break;
		}
		return blueprint;
	}
}