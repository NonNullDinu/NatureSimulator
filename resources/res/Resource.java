package res;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Resource {
	private String location;
	private InputStream asInputStream;
	private boolean exists;

	public Resource(String location) {
		this.location = location;
		this.asInputStream = Resource.class.getResourceAsStream(location.replace("res/", ""));
		if (this.asInputStream == null) {
			this.asInputStream = ClassLoader.getSystemResourceAsStream("res/" + location);
			if (this.asInputStream == null) {
				try {
					this.asInputStream = new FileInputStream(location);
				} catch (FileNotFoundException e) {
				}
			}
		}
		this.exists = this.asInputStream != null;
	}

	public String getLocation() {
		return location;
	}

	public InputStream asInputStream() {
		if (asInputStream != null)
			return asInputStream;
		else
			throw new NullPointerException("File at " + location + " could not be found");
	}

	public boolean exists() {
		return exists;
	}
}
