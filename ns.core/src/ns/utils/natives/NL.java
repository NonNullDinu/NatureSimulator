package ns.utils.natives;

/**
 * Class that parses java objects into objects that can be used in native code
 * 
 * @author Dinu
 */
public class NL /* NativeLibrary */ {
	static {
//		System.load(GU.path + "lib/natives/libnl.so");
	}

	public static Object nativeCall(Method method, Object... objects) {
		return method.call(objects);
	}

	public enum Method {
		ADD((Object... o) -> _NL.add((int) o[0], (int) o[1])),
		;
		private MethodImpl implementation;

		Method(MethodImpl implementation) {
			this.implementation = implementation;
		}

		private Object call(Object[] args) {
			return implementation.call(args);
		}
	}

	private interface MethodImpl {
		Object call(Object[] args);
	}
}