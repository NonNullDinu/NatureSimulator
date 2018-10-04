package ns.components;

import ns.openglWorkers.ModelsLibrary;
import ns.world.Biome;

import java.io.IOException;
import java.io.InputStream;

public class BlueprintInputStream {
	private InputStream in;

	public BlueprintInputStream(InputStream in) {
		this.in = in;
	}

	public Blueprint read() throws IOException {
		byte[] data = new byte[6];
		in.read(data);

		int dataIn = data[0];
		String enFolder = Integer.toString(999 + dataIn);
		String modelName = "";
		switch (dataIn) {
			case 1:
			case 2:
			case 3:
			case 5:
			case 6:
			case 7:
			case 11:
				modelName = "tree";
				break;
			case 4:
				modelName = "mushroom";
				break;
			case 8:
				modelName = "grass";
				break;
			case 9:
				modelName = "stone";
				break;
			case 10:
				modelName = "seaWeed";
				break;
			case 12:
				modelName = "snowman";
				break;
			case 13:
				modelName = "sheep";
				break;
			case 14:
				modelName = "meat";
				break;
		}
		Blueprint blueprint = new Blueprint(enFolder);
		blueprint.withModel(new ModelComponent(
				ModelsLibrary.getModel("models/" + enFolder + "/" + modelName + ".mdl")));
		if (data[1] >> 4 != 0) {
			blueprint.withMovement(new MovementComponent(data[1] >> 4));
		}
		if ((data[1] & 15) != 0) {
			blueprint.withBiomeSpread(
					new BiomeSpreadComponent().withBiome(Biome.get(data[1] & 15)).withMinMaxRange(data[2], data[3]));
		}
		if (data[4] != 0) {
			blueprint.withLife(new LifeComponent(data[4] << 8 + data[5]));
		}
		blueprint.withDefaultCustomColors();
		return blueprint;
	}
}
