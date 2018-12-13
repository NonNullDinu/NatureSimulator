package resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class In {

	private PATH location;
	private InputStream asInputStream;
	private boolean exists;
	private int ver;
	private boolean hasVersion;

	private In() {
	}

	public static In create(String location) {
		return new In().withLocation(location).create();
	}

	public static In create(String location, boolean version) {
		return new In().withLocation(location).withVersion(version).create();
	}

	private In withLocation(String location) {
		return withLocation(new PATH(location));
	}

	private In withLocation(PATH location) {
		this.location = location;
		return this;
	}

	private In withVersion(boolean version) {
		this.hasVersion = version;
		return this;
	}

	private In create() {
		try {
			this.asInputStream = location.openInput();
			exists = true;
		} catch (FileNotFoundException ignored) {
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

	public PATH getLocation() {
		return location;
	}

	public InputStream asInputStream() throws NullPointerException {
		if (asInputStream != null)
			return asInputStream;
		else
			throw new NullPointerException("File at " + location.relativeLocation() + " could not be found");
	}

	public boolean exists() {
		return exists;
	}

	public int version() {
		return ver;
	}
}
