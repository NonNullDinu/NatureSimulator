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

package ns.utils.natives;

import ns.utils.GU;

/**
 * Class that parses java objects into objects that can be used in native code
 *
 * @author Dinu
 */
public class NL /* NativeLibrary */ {
	private static final NL_ENV env;

	static {
		System.load(GU.path + "lib/natives/libnl.so");
		if (GU.OS_LINUX) {
			env = new NL_LINUX_ENV();
		} else if (GU.OS_WINDOWS) {
			env = new NL_WIN_ENV();
		} else {
			env = null;
		}
	}

	public static Object nativeCall(NativeMethod method, Object... objects) {
		return env.call(method, objects);
	}
}