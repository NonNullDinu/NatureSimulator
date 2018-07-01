package ns.copyGameData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyGameData {

	public static void main(String[] args) {
		copy(new File(System.getProperty("user.dir") + "/gameData"));
	}

	private static void copy(File file) {
		File target = new File(file.getAbsolutePath().replace("NatureSimulator/gameData/",
				"NS_Installer/src/ns/mainEngine/install/gameData/"));
		if (file.isDirectory()) {
			target.mkdir();
			for (File f : file.listFiles())
				copy(f);
		} else {
			try {
				FileInputStream fin = new FileInputStream(file);
				FileOutputStream fout = new FileOutputStream(target);
				fout.write(fin.readAllBytes());
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}