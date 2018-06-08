package ns.worldSave.NSSV1100;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import ns.entities.Entity;
import ns.terrain.Terrain;
import ns.world.World;
import ns.worldSave.EndObject;
import ns.worldSave.EntityData;
import ns.worldSave.NSSVFile;
import ns.worldSave.TerrainData;
import res.Resource;

public class NSSV1100File extends NSSVFile {

	public NSSV1100File(Resource resource) {
		super(resource);
	}

	@Override
	protected World load(InputStream ins) {
		World world = null;
		long begin = System.nanoTime();
		try {
			ObjectInputStream stream = new ObjectInputStream(ins);
			Object o;
			List<Entity> entities = new ArrayList<>();
			Terrain terrain = null;
			while (true) {
				o = stream.readObject();
				if (o instanceof EntityData)
					entities.add(((EntityData) o).asInstance());
				else if (o instanceof TerrainData)
					terrain = ((TerrainData) o).asInstance();
				else if (o instanceof EndObject)
					break;
			}
			world = new World(entities, terrain);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("World load time " + (System.nanoTime() - begin));
		return world;
	}
}