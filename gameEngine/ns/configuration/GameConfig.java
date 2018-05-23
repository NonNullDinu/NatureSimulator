package ns.configuration;

import java.util.HashMap;
import java.util.Map;

public class GameConfig {
	public static final int FULLSCREEN = 1;
	
	private static final Map<Integer, Config> configuration = new HashMap<>();
	private static final Map<Integer, Float> configurationf = new HashMap<>();
	
	static {
		configuration.put(FULLSCREEN, Config.FALSE);
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