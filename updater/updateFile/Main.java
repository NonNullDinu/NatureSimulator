package updateFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import updateFile.functions.Function;

public class Main {
	/**
	 * To update the program, run this. Type into the console the name / the
	 * relative path within the project folder of the update file(".update" is added by
	 * the program, so do NOT type it in the console)
	 * 
	 * @param args
	 *            NOT needed in the application
	 * @throws IOException
	 *             If any IOException occurs
	 */
	public static void main(String[] args) throws IOException {
		byte[] file = new byte[200];
		int len = System.in.read(file);
		String fl = "";
		for (int i = 0; i < len - 1; i++) {
			fl += (char) file[i];
		}
		String path = System.getProperty("user.dir") + "/";
		BufferedReader reader = new BufferedReader(new FileReader(new File(path + fl + ".update")));
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
				i = exec(line, path, lines, i);
		}
		reader.close();
	}

	private static int exec(String line, String path, List<String> lines, int iterationIndex) throws IOException {
		if (line.startsWith("if(")) {
			String condition = line.substring(3, line.lastIndexOf((int) ')'));
			String actions = "", elss = "";
			boolean writeToElse = false;
			int end;
			for (int i = iterationIndex + 1;; i++) {
				String ln = lines.get(i);
				if (ln.contains("then: "))
					ln = ln.replace("then: ", "");
				if (ln.contains("else: ")) {
					writeToElse = true;
					ln = ln.replace("else: ", "");
				}
				if (ln.startsWith("endif")) {
					end = i;
					break;
				}
				if (writeToElse)
					elss += ln + "\n";
				else
					actions += ln + "\n";
			}
			if (condition.startsWith("$f:\"") && condition.endsWith("\".exists()") && condition.contains("/*.")) {
				String fileName = condition.replace("$f:\"", "").replace("/*\".exists()", "");
				File dir = new File(fileName);
				boolean foundOneFile = false;
				for (File f : dir.listFiles())
					if (f.getName().endsWith(
							condition.substring(condition.indexOf("."), condition.lastIndexOf((int) '.') - 2)))
						foundOneFile = true;
				if (foundOneFile) {
					String[] aclines = actions.split("\n");
					for (int i = 0; i < aclines.length; i++)
						exec(aclines[i], path, lines, iterationIndex + i);
					return end + 1;
				} else {
					String[] aclines = elss.split("\n");
					for (int i = 0; i < aclines.length; i++)
						exec(aclines[i], path, lines, iterationIndex + i);
					return end + 1;
				}
			} else if (condition.startsWith("$f:\"") && condition.endsWith("/*\".exists()")) {
				String fileName = condition.replace("$f:\"", "").replace("/*\".exists()", "");
				File dir = new File(fileName);
				boolean foundOneFile = dir.listFiles().length > 0;
				if (foundOneFile) {
					String[] aclines = actions.split("\n");
					for (int i = 0; i < aclines.length; i++)
						exec(aclines[i], path, lines, iterationIndex + i);
					return end + 1;
				} else {
					String[] aclines = elss.split("\n");
					for (int i = 0; i < aclines.length; i++)
						exec(aclines[i], path, lines, iterationIndex + i);
					return end + 1;
				}
			} else if (condition.startsWith("$f:\"") && condition.endsWith("\".exists()")) {
				String fileName = condition.replace("$f:\"", "").replace("\".exists()", "");
				if (new File(path + fileName).exists()) {
					String[] aclines = actions.split("\n");
					for (int i = 0; i < aclines.length; i++)
						exec(aclines[i], path, lines, iterationIndex + i);
					return end + 1;
				} else {
					String[] aclines = elss.split("\n");
					for (int i = 0; i < aclines.length; i++)
						exec(aclines[i], path, lines, iterationIndex + i);
					return end + 1;
				}
			} else {
				System.err.println(
						"Could not understand condition:" + condition + " at line " + iterationIndex + ". Exit");
				System.exit(-1);
			}
		} else if (line.startsWith("new ")) {
			String[] pcs = line.split(" ");
			Function.CREATE_NEW.callWithArgs(path, line.replace(pcs[0] + " ", ""));
		} else if (line.startsWith("echo ")) {
			String[] pcs = line.split(" ");
			Function.ECHO.callWithArgs(path, line.replace(pcs[0] + " ", ""));
		} else if (line.startsWith("del ")) {
			String[] pcs = line.split(" ");
			Function.DELETE.callWithArgs(path, line.replace(pcs[0] + " ", ""));
		} else if (line.startsWith("write_new")) {
			String[] pcs = line.split(" ");
			Function.WRITE_NEW.callWithArgs(path, line.replace(pcs[0] + " ", ""));
		}
		return iterationIndex;
	}
}