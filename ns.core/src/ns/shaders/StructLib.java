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

package ns.shaders;

import ns.utils.GU;
import resources.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StructLib {
	private static final Map<String, String> structs = new HashMap<>();

	public static void load(In resource) {
		BufferedReader reader = GU.open(resource);
		try {
			String line;
			String currentStructName = null, structBody = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("}")) {
					structBody += "};";
					structs.put(currentStructName, structBody.replaceAll("\n", ""));
				} else if (line.startsWith("struct ")) {
					currentStructName = line.split(" ")[1];
					if (currentStructName.contains("{")) {
						currentStructName = currentStructName.replace("{", "");
					}
					structBody = line + "\n";
				} else if (!line.equals(""))
					structBody += line + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String get(String name) {
		return structs.get(name);
	}
}