package ns.utils.natives;

public class NL_LINUX_ENV extends NL_ENV {

	@Override
	public void loadNativeDependencies(String libFolder) {
		System.load(libFolder + "/libns.so");
	}

	@Override
	protected native Object call(NativeMethod method, Object[] objects);
}