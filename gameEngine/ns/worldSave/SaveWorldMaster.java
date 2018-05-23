package ns.worldSave;

import java.io.IOException;
import java.io.ObjectOutputStream;

import ns.entities.Entity;
import ns.world.World;
import res.WritingResource;

public class SaveWorldMaster {
	public static void save(World world, WritingResource resource) {
		ObjectOutputStream stream = null;
		try {
			stream = new ObjectOutputStream(resource.asOutputStream());
			for (Entity entity : world.getEntities())
				stream.writeObject(entity.asData());
			stream.writeObject(world.getTerrain().asData());
			stream.writeObject(new EndObject());
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}