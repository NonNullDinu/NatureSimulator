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

import ns.utils.GU;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

class ConfigInputStream {
	private static final int CONF_TYPE = GU.binaryInt("0011 0000");
	private static final int CONF = GU.binaryInt("0000 0111");

	private final InputStream instream;

	public ConfigInputStream(InputStream src) {
		this.instream = src;
	}

	private Config read() throws IOException {
		byte[] data = new byte[2];
		instream.read(data, 0, 2);
		int flags = data[0];
		int configType = (flags & CONF_TYPE) >> 4;
		int conf = (flags & CONF);
		Config config = null;
		if (configType == 1) {
			config = (conf == 0 ? Config.FALSE : Config.TRUE);
		}
		return config;
	}

	public void readTo(Map<Integer, Config> configuration) throws IOException {
		for (int i = 0; i < GameConfig.TOTAL; i++)
			configuration.put(i, read());
	}
}