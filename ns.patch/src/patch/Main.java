package patch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) throws IOException {
		String path = System.getProperty("user.dir") + "/";
		if (!System.getProperty("os.name").equals("Linux")/* || (args.length > 0 && args[0].equals("--use-ns-update-script"))*/) {
			BufferedReader reader = new BufferedReader(new FileReader(new File(path + ".nus")));
			String line;
			List<String> lines = new ArrayList<>();
			while ((line = reader.readLine()).compareTo("FINISHED") != 0) {
				lines.add(line);
			}
			for (int i = 0; i < lines.size(); i++) {
				line = lines.get(i);
				if (line.startsWith("cd")) {
					if (line.equals("cd..")) {
						String[] pcs = path.split("/");
						path = path.replace(pcs[pcs.length - 1] + "/", "");
					} else
						path += line.split(" ")[1] + "/";
				} else
					exec(line, path);
			}
			reader.close();
		} else {
			Process p = Runtime.getRuntime().exec("cd \"$(cat ~/.ns-install/target-dir)\" && wget -O update.sh https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/updates/latestUpdate.sh && sh update.sh && rm update.sh");
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (p.exitValue() > 0) {
				System.err.println(new String(p.getErrorStream().readAllBytes()));
			}
		}
	}

	private static void exec(String line, String path) throws IOException {
		if (line.startsWith("new ")) {
			String[] pcs = line.split(" ");
			Function.CREATE_NEW.callWithArgs(path, line.replace(pcs[0] + " ", ""));
		} else if (line.startsWith("echo ")) {
			String[] pcs = line.split(" ");
			Function.ECHO.callWithArgs(path, line.replace(pcs[0] + " ", ""));
		} else if (line.startsWith("del ")) {
			String[] pcs = line.split(" ");
			Function.DELETE.callWithArgs(path, line.replace(pcs[0] + " ", ""));
		} else if (line.startsWith("write_new ")) {
			String[] pcs = line.split(" ");
			Function.WRITE_NEW.callWithArgs(path, line.replace(pcs[0] + " ", ""));
		}
	}
}