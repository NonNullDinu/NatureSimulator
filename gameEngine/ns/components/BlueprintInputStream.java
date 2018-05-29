package ns.components;

import java.io.IOException;
import java.io.InputStream;

import ns.openglWorkers.ModelsLibrary;
import ns.world.Biome;

public class BlueprintInputStream {
	private InputStream in;
	
	public BlueprintInputStream(InputStream in) {
		this.in = in;
	}
	
	public Blueprint read() throws IOException {
		byte[] data = new byte[4];
		in.read(data, 0, 4);
		
		int dataIn = data[0];
		String enFolder = Integer.toString(999 + dataIn);
		String modelName = "";
		if(dataIn == 1 || dataIn == 2 || dataIn == 3) {
			modelName = "tree";
		}
		if(dataIn == 4) {
			modelName = "mushroom";
		}
		Blueprint blueprint = new Blueprint(enFolder);
		blueprint.withModel(new ModelComponent(ModelsLibrary.getModel("res/models/" + enFolder + "/" + modelName + ".obj")));
		if(data[1] >> 4 != 0) {
			blueprint.withMovement(new MovementComponent(data[1] >> 4));
		}
		if((data[1] & 15) != 0) {
			blueprint.withBiomeSpread(new BiomeSpreadComponent().withBiome(Biome.get(data[1] & 15)).withMinMaxRange(data[2], data[3]));
		}
		blueprint.withDefaultCustomColors();
		return blueprint;
	}
}
