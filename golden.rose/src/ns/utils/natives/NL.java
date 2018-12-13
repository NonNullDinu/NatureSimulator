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