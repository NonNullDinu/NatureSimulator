package ns.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ns.utils.GU;

public class ConfigInputStream {
	private static final int CONF_TYPE = GU.binaryInt("0011 0000");
	private static final int CONF = GU.binaryInt("0000 0111");

	private InputStream instream;

	public ConfigInputStream(InputStream src) {
		this.instream = src;
	}

	public Config read() throws IOException {
		byte[] data = new byte[2];
		instream.read(data, 0, 2);
		int flags = data[0];
		int configType = (flags & CONF_TYPE) >> 4;
		int conf = (flags & CONF);
		Config config = null;
		if (configType == 1) {
			config = (conf == 0 ? Config.FALSE : Config.TRUE);
		} else if (configType == 2) {
			config = Config.VALUE;
		}
		return config;
	}

	public void readTo(Map<Integer, Config> configuration) throws IOException {
		for (int i = 0; i < GameConfig.TOTAL; i++)
			configuration.put(i, read());
	}
}