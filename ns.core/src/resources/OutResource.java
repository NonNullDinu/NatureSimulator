package resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WritingResource {
	private String location;
	private OutputStream outputStream;

	public WritingResource() {
	}
	
	public WritingResource withLocation(String location) {
		this.location = location;
		return this;
	}
	
	public WritingResource create() {
		File f = new File(location);
		if(!f.exists())
			try {
				if(!f.getParentFile().exists())
					f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				throw new InstantiationError("Could not create non-existent file at " + location + ", " + e.getLocalizedMessage());
			}
		try {
			this.outputStream = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public void writeVersion(int version) throws IOException {
		this.outputStream.write(version);
	}
	
	public OutputStream asOutputStream() {
		return outputStream;
	}
	
	public String getLocation() {
		return location;
	}
}