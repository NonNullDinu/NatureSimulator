package resources;

import ns.utils.GU;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PATH {
	private _File file;
	private String loc;
	private String relativeLocation;

	public PATH(String location) {
		this(new _File(location));
	}

	public PATH(_File file) {
		this.file = file;
		this.loc = file.getAbsolutePath();
		this.relativeLocation = file.getAbsolutePath().replaceFirst(GU.path, "");
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
