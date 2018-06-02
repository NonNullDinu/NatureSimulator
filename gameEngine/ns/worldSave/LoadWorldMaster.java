package ns.worldSave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import ns.entities.Entity;
import ns.terrain.Terrain;
import ns.world.World;
import res.Resource;

public class LoadWorldMaster {
	public static  World loadWorld(Resource res) {
		World world = null;
		try {
			ObjectInputStream stream = new ObjectInputStream(res.asInputStream());
			Object o;
			List<Entity> entities = new ArrayList<>();
			Terrain terrain = null;
			while(true) {
				o = stream.readObject();
				if(o instanceof EntityData)
					entities.add(((EntityData) o).asInstance());
				else if(o instanceof TerrainData)
					terrain = ((TerrainData) o).asInstance();
				else if(o instanceof EndObject)
					break;
			}
			world = new World(entities, terrain);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return world;
	}
}