package ns.customFileFormat;

import ns.exceptions.LoadingException;

import java.lang.reflect.InvocationTargetException;

public enum FileFormat {
	TEXTURE("tex", TexFile.class), AUDIO("aud", AudFile.class), MODEL("mdl", MdlFile.class);

	private final String extension;
	private final Class<? extends File> representingClass;

	FileFormat(String extension, Class<? extends File> representingClass) {
		this.extension = extension;
		this.representingClass = representingClass;
	}

	public String getExtension() {
		return extension;
	}

	public Object load(String location) throws LoadingException {
		try {
			File f = representingClass.getConstructor(String.class).newInstance(location);
			return f.load();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}