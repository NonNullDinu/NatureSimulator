package ns.utils.natives;

/**
 * Class that parses java objects into objects that can be used in native code
 * 
 * @author Dinu
 */
public class NL /* NativeLibrary */ {
	static {
		// TODO Load library
	}

	public static Object nativeCall(Method method, Object... objects) {
		return method.call(objects);
	}

	public enum Method {
		;
		private MethodImpl implementation;

		private Method(MethodImpl implementation) {
			this.implementation = implementation;
		}

		private Object call(Object[] args) {
			return implementation.call(args);
		}
	}

	private interface MethodImpl {
		abstract Object call(Object[] args);
	}
}