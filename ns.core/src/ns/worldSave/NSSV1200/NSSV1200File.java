package ns.worldSave.NSSV1200;

import ns.entities.Entity;
import ns.rivers.RiverList;
import ns.terrain.Terrain;
import ns.world.World;
import ns.worldSave.EndObject;
import ns.worldSave.EntityData;
import ns.worldSave.NSSVFile;
import ns.worldSave.TerrainData;
import resources.In;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class NSSV1200File extends NSSVFile {

	public NSSV1200File(In resource) {
		super(resource);
	}

	@Override
	protected World load(InputStream ins) {
		World world = null;
		try {
			ObjectInputStream stream = new ObjectInputStream(ins);
			Object o;
			List<Entity> entities = new ArrayList<>();
			Terrain terrain = null;
			RiverList rivers = new RiverList();
			while (true) {
				o = stream.readObject();
				if (o instanceof EntityData)
					entities.add(((EntityData) o).asInstance());
				else if (o instanceof TerrainData)
					terrain = ((TerrainData) o).asInstance();
				else if(o instanceof RiverList)
					rivers = (RiverList) o;
				else if (o instanceof EndObject)
					break;
			}
			world = new World(entities, terrain);
			world.setRivers(rivers);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return world;
	}
}