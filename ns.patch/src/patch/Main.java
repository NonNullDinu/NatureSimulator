package patch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Main {
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
			byte[] bts = new FileInputStream(System.getProperty("user.home") + "/.ns-install/target-dir").readAllBytes();
			String installDir = new String(bts, 0, bts.length - 1);
			System.out.println(installDir);
			Process p = Runtime.getRuntime().exec("sh " + System.getProperty("user.home") + "/.ns-install/update.sh");
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(new String(p.getInputStream().readAllBytes()));
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