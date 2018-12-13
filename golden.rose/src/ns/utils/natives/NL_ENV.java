package ns.utils.natives;

public abstract class NL_ENV {
	public abstract void loadNativeDependencies(String libFolder);

	protected abstract Object call(NativeMethod method, Object[] objects);
}