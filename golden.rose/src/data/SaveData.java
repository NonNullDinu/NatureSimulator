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

package data;

import ns.utils.GU;
import resources.IReadWriteFolder;
import resources.In;
import resources.Out;

public class SaveData implements IReadWriteFolder {
	private static SaveData folder;

	private SaveData() {
	}

	public static void init() {
		folder = new SaveData();
	}

	public static In getResourceAt(String location) {
		return folder._getResourceAt(location);
	}

	public static In getResourceAt(String location, boolean version) {
		return folder._getResourceAt(location, version);
	}

	public static Out openOutput(String location) {
		return folder._openOutput(location);
	}

	@Override
	public In _getResourceAt(String location) {
		return In.create("saveData/" + location);
	}

	@Override
	public In _getResourceAt(String location, boolean version) {
		return In.create("saveData/" + location, version);
	}

	@Override
	public Out _openOutput(String location) {
		return Out.create(GU.path + "/saveData/" + location);
	}
}