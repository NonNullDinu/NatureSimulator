package ns.worldSave;

import ns.utils.GU;
import ns.world.World;
import resources.Out;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveWorldMaster {
	public static void save(World world, Out resource) {
		ObjectOutputStream stream;
		try {
			resource.writeVersion(GU.CURRENT_WORLD_FILE_VERSION);
			stream = new ObjectOutputStream(resource.asOutputStream());
			for (int i = 0;i < world.getEntities().size(); i++)
				stream.writeObject(world.getEntities().get(i).asData());
			stream.writeObject(world.getRivers());
			stream.writeObject(world.getTerrain().asData());
			stream.writeObject(new EndObject(GU.time));
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}