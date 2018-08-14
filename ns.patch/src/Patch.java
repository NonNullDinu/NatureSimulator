import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Patch implements Serializable {
	private byte[] byteData;

	private void apply(File installDirectory) {
		File updateTmp = new File(installDirectory.getAbsolutePath() + "/updateTmp");
		updateTmp.mkdir();
		int i = 0;
		List<String> filesToUpdate = new ArrayList<>();
		while (i < byteData.length) {
			if (byteData[i] == 0 && byteData[i + 1] == 1) {
				i += 2;
				String path = "";
				for (; byteData[i] != 0 || byteData[i + 1] != 0; i++) // Read path data
					path += (char) byteData[i];
				filesToUpdate.add(path);
				FileOutputStream outStr = null;
				try {
					outStr = new FileOutputStream(updateTmp.getAbsolutePath() + "/" + path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				i += 2;
				List<Byte> bytesToFile = new ArrayList<>();
				for (; byteData[i] != 0 || byteData[i + 1] != 0; i++) // Read file data
					bytesToFile.add(byteData[i]);
				i += 2;
				try {
					for (byte b : bytesToFile)
						outStr.write(b);
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
			i++;
		}
		List<String> jarFilesToUpdate = new ArrayList<>();
		List<String> others = new ArrayList<>();
		for (String s : filesToUpdate)
			if (s.startsWith("jar"))
				jarFilesToUpdate.add(s.substring(3));
			else
				others.add(s);
		String args = "";
		for (i = 0; i < jarFilesToUpdate.size(); i++)
			args += " " + jarFilesToUpdate.get(i);
		try {
			Process p =
					Runtime.getRuntime().exec("cd " + updateTmp.getAbsolutePath() + " &&jar uf " + installDirectory.getAbsolutePath() + "/NatureSimulator.jar" + args);
			p.waitFor();
			if (p.exitValue() != 0)
				System.err.println("Error occurred while updating jar");
			for (i = 0; i < others.size(); i++) {
				args = others.get(i);
				p =
						Runtime.getRuntime().exec("cd " + updateTmp.getAbsolutePath() + " &&rm " + installDirectory.getAbsolutePath() + "/gameData" +
								"/" + args + " &&mv " + args +
								" " +
								installDirectory.getAbsolutePath() + "/gameData" +
								"/" + args);
				p.waitFor();
				if (p.exitValue() != 0)
					System.err.println("Error occurred while updating " + args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}