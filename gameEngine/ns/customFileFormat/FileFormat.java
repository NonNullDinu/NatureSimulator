package ns.customFileFormat;

import java.lang.reflect.InvocationTargetException;

import ns.exceptions.LoadingException;

public enum FileFormat {
	TEXTURE("tex", TexFile.class);

	private String extension;
	private Class<? extends File> representingClass;

	private FileFormat(String extension, Class<? extends File> representingClass) {
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