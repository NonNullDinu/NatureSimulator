/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
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

package ns.worldSave.NSSV1100;

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
import java.util.Objects;

public class NSSV1100File extends NSSVFile {

	public NSSV1100File(In resource) {
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
				else if (o instanceof RiverData)
					rivers.add(((RiverData) o).asInstance());
				else if (o instanceof EndObject)
					break;
			}
			world = new World(entities, Objects.requireNonNull(terrain));
			world.setRivers(rivers);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return world;
	}
}