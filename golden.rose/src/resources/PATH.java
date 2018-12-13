package resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PATH {
	private final _File file;
	private final String relativeLocation;

	public PATH(String location) {
		this(new _File(location));
	}

	public PATH(@SuppressWarnings("exports") _File file) {
		this.file = file;
		this.relativeLocation = file.locGiven;
	}

	public FileInputStream openInput() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public FileOutputStream openOutput() throws IOException {
		if (!file.exists()) {
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return new FileOutputStream(file);
	}

	public String relativeLocation() {
		return relativeLocation;
	}
}
