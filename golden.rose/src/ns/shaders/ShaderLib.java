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

import data.GameData;
import ns.utils.GU;
import resources.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShaderLib {
	private static final Map<String, String> sources = new HashMap<>();

	public static void loadAll() {
		List<In> shaders = new ArrayList<>();
		addDeclarations(shaders);
		for (In shaderFile : shaders) {
			BufferedReader reader = GU.open(shaderFile);
			String line, source = "";
			try {
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("#Struct_Lib.")) {
						line = StructLib.get(line.replace("#Struct_Lib.", ""));
					}
					source += line + "\n";
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sources.put(shaderFile.getLocation().relativeLocation(), source);
		}
	}

	private static void addDeclarations(List<In> shaders) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(GameData.getResourceAt("shaders/shaders.dir").asInputStream()));
		String fld = "";
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("./"))
					shaders.add(GameData.getResourceAt("shaders/" + fld + line.substring(1)));
				else
					fld = line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getSource(String shaderName) {
		return sources.get(shaderName);
	}
}