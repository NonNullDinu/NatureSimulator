package resources;

import java.io.IOException;
import java.io.OutputStream;

public class Out {
	private PATH location;
	private OutputStream outputStream;

	private Out() {
	}

	public Out withLocation(String location) {
		return withLocation(new PATH(location));
	}

	public Out withLocation(PATH location) {
		this.location = location;
		return this;
	}

	public Out create() {
		try {
			this.outputStream = location.openOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public static Out create(String location) {
		return new Out().withLocation(location).create();
	}
	
	public void writeVersion(int version) throws IOException {
		this.outputStream.write(version);
	}
	
	public OutputStream asOutputStream() {
		return outputStream;
	}

	public PATH getLocation() {
		return location;
	}
}