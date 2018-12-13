package ns.utils.natives;

public class NL_WIN_ENV extends NL_ENV {

	@Override
	public void loadNativeDependencies(String libFolder) {
		System.load(libFolder + "/libns.dll");
	}

	@Override
	protected Object call(NativeMethod method, Object[] objects) {
		return null;
	}
}