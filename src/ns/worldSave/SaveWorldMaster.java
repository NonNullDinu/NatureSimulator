package ns.worldSave;

import java.io.IOException;
import java.io.ObjectOutputStream;

import ns.utils.GU;
import ns.world.World;
import resources.WritingResource;

public class SaveWorldMaster {
	public static void save(World world, WritingResource resource) {
		ObjectOutputStream stream = null;
		try {
			resource.writeVersion(GU.CURRENT_WORLD_FILE_VERSION);
			stream = new ObjectOutputStream(resource.asOutputStream());
			for (int i = 0;i < world.getEntities().size(); i++)
				stream.writeObject(world.getEntities().get(i).asData());
//			for(River river : world.getRivers())
//				stream.writeObject(river.asData());
			stream.writeObject(world.getRivers());
			stream.writeObject(world.getTerrain().asData());
			stream.writeObject(new EndObject());
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}