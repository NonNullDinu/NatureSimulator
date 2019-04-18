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
			for (int i = 0; i < world.getEntities().size(); i++)
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