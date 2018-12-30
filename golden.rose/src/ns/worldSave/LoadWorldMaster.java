/*
 * Copyright (C) 2018  Dinu Blanovschi
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

import ns.world.World;
import ns.worldSave.NSSV1000.NSSV1000File;
import ns.worldSave.NSSV1100.NSSV1100File;
import ns.worldSave.NSSV1200.NSSV1200File;
import ns.worldSave.NSSV1300.NSSV1300File;
import resources.In;

public class LoadWorldMaster {
	public static World loadWorld(In res) {
		int ver = res.version();
		World world = null;
		if (ver == 1)
			world = new NSSV1000File(res).load();
		else if (ver == 2)
			world = new NSSV1100File(res).load();
		else if (ver == 3)
			world = new NSSV1200File(res).load();
		else if (ver == 4)
			world = new NSSV1300File(res).load();
		return world;
	}
}