package ns.configuration;

import data.GameData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class GameConfig {
	public static final int FULLSCREEN = 0;

	private static final Map<Integer, Config> configuration = new HashMap<>();

	static final int TOTAL = 1;

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