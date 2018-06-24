package res;

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

	public Resource() {
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
		} catch (FileNotFoundException e1) {
			try {
				this.asInputStream = new FileInputStream(GU.path + "gameData/" + location);
				exists = true;
			} catch (FileNotFoundException e) {
				exists = false;
			}
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
