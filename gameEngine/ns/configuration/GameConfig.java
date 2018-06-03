package ns.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import res.Resource;

public class GameConfig {
	public static final int FULLSCREEN = 0;
	
	private static final Map<Integer, Config> configuration = new HashMap<>();
	private static final Map<Integer, Float> configurationf = new HashMap<>();

	protected static final int TOTAL = 1;
	
	static {
		ConfigInputStream inp = new ConfigInputStream(new Resource("config/gameConfiguration.config").asInputStream());
		try {
			inp.readTo(configuration);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setConfig(int key, Config value) {
		configuration.replace(key, value);
	}
	
	public static void setConfigf(int key, float value) {
		configurationf.replace(key, value);
	}
	
	public static Config getConfig(int key) {
		return configuration.get(key);
	}
	
	public static float getConfigurationf(int key) {
		return configurationf.get(key);
	}
}