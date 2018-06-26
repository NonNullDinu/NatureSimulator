package ns.worldSave;

import java.io.IOException;
import java.io.ObjectOutputStream;

import ns.entities.Entity;
import ns.rivers.River;
import ns.utils.GU;
import ns.world.World;
import res.WritingResource;

public class SaveWorldMaster {
	public static void save(World world, WritingResource resource) {
		ObjectOutputStream stream = null;
		try {
			resource.writeVersion(GU.CURRENT_WORLD_FILE_VERSION);
			stream = new ObjectOutputStream(resource.asOutputStream());
			for (Entity entity : world.getEntities())
				stream.writeObject(entity.asData());
			stream.writeObject(world.getTerrain().asData());
			for(River river : world.getRivers())
				stream.writeObject(river.asData());
			stream.writeObject(new EndObject());
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}