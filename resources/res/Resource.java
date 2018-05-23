package res;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Resource {
	private String location;
	private InputStream asInputStream;
	private final boolean exists;

	public Resource(String location) {
		this.location = location;
		try {
			this.asInputStream = new DataInputStream(new FileInputStream(new File(location)));
		} catch (FileNotFoundException e) {
			this.asInputStream = ClassLoader.getSystemResourceAsStream(this.location);
		}
		this.exists = this.asInputStream != null;
	}

	public String getLocation() {
		return location;
	}

	public InputStream asInputStream() {
		return asInputStream;
	}

	public boolean exists() {
		return exists;
	}
}
