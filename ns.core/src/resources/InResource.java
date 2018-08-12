package resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ns.utils.GU;

public class Resource {
	
	private String location;
	private InputStream asInputStream;
	private boolean exists;
	private int ver;
	private boolean hasVersion;

	private Resource() {
	}
	
	public static Resource create(String location) {
		return new Resource().withLocation(location).create();
	}
	
	public static Resource create(String location, boolean version) {
		return new Resource().withLocation(location).withVersion(version).create();
	}

	public Resource withLocation(String location) {
		this.location = location;
		return this;
	}

	public Resource withVersion(boolean version) {
		this.hasVersion = version;
		return this;
	}

	public Resource create() {
		try {
			this.asInputStream = new FileInputStream(GU.path + location);
			exists = true;
		} catch (FileNotFoundException e) {
		}
		if (this.exists && this.hasVersion) {
			try {
				this.ver = asInputStream.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
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

	public int version() {
		return ver;
	}
}
