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

package ns.openglWorkers;

import ns.customFileFormat.MdlFile;
import ns.openglObjects.VAO;
import obj.OBJLoader;

import java.util.HashMap;
import java.util.Map;

public class ModelsLibrary {
	private static final Map<String, VAO> models = new HashMap<>();

	public static VAO getModel(String name) {
		VAO model = models.get(name);
		if (model == null) {
			if (name.endsWith(".mdl"))
				model = new MdlFile(name).load();
			else
				model = OBJLoader.loadObj(name);
			models.put(name, model);
		}
		return model;
	}
}