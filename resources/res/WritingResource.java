package res;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WritingResource {
	private String location;
	private DataOutputStream outputStream;

	public WritingResource(String location) {
		this.location = location;
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
			this.outputStream = new DataOutputStream(new FileOutputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public DataOutputStream asOutputStream() {
		return outputStream;
	}
	
	public String getLocation() {
		return location;
	}
}