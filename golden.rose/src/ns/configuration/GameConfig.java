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

package ns.configuration;

import data.GameData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameConfig {
	public static final int FULLSCREEN = 0;
	static final int TOTAL = 1;
	private static final Map<Integer, Config> configuration = new HashMap<>();

	static {
		ConfigInputStream inp = new ConfigInputStream(GameData.getResourceAt("config/gameConfiguration.config").asInputStream());
		try {
			inp.readTo(configuration);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setConfig(int key, Config value) {
		configuration.replace(key, value);
	}

	public static Config getConfig(int key) {
		return configuration.get(key);
	}
}