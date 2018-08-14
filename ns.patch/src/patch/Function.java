package patch;

import java.io.*;

public enum Function {
	/**
	 * Deletes a file/directory specified by the first argument
	 * 
	 * @param 1
	 *            The file name / relative path at the current path variable
	 */
	DELETE("del", true),
	/**
	 * Creates a new file/directory specified in the current directory/in relative
	 * path to current directory by the second argument
	 * 
	 * @param 1
	 *            The type (--type=folder for folder and --type=file for file)
	 * @param 2
	 *            The name/The relative path of the folder/file
	 */
	CREATE_NEW("new", true),
	/**
	 * Writes to a file, if the file does not exist, then it creates one
	 * 
	 * @param 1
	 *            The name/The relative path to the current directory of the file
	 * @param 2
	 *            The content of the file. To get this to work, @see Function.ECHO
	 *            --target=file
	 */
	WRITE_NEW("write_new", true),
	/**
	 * Writes to a file/console
	 * 
	 * @param 1
	 *            The target of the output(--target=console for console and
	 *            --target=file for file)
	 * @param 2
	 *            Depends on the first parameter, if it is --target=console, then it
	 *            prints every character after the space of the echo word, else if
	 *            it is --target=file, then the parameter must be the file name/
	 *            relative path to current directory
	 * @param 3
	 *            For --target=file, this is the content that is going to be written
	 *            to the file, but the content should have replaced all ' ' with
	 *            "%0" and '\n' with "%1"
	 */
	ECHO("echo", true);

	private String updateFunctionName;
	private String source;

	/**
	 * 
	 * @param updateFunctionName
	 *            The name of the function. If it is not a core .update function,
	 *            then it is searched for in updater/updateFile/functions and loads
	 *            the source from there
	 * @param definedInCallWithArgs
	 *            A boolean indicating if it is a core .update function or a
	 *            user-defined one
	 */

	Function(String updateFunctionName, boolean definedInCallWithArgs) {
		this.updateFunctionName = updateFunctionName;
		if (!definedInCallWithArgs) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader
					.getSystemResourceAsStream("updateFile/functions/" + updateFunctionName + ".updateFunction")));
			String line;
			try {
				while ((line = reader.readLine()) != null)
					source += line + "\n";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * @param path The current path
	 * @param args The command arguments separated by ' '
	 * @throws IOException If any IOException occurs
	 * @throws NullPointerException If path / args is null
	 */
	public void callWithArgs(String path, String args) throws IOException, NullPointerException {
		if(path == null || args == null)
			throw new NullPointerException();
		switch (updateFunctionName) {
		case "del":
			if (args.endsWith("/*")) {
				String dir = args.substring(0, args.length() - 2);
				File directory = new File(path + dir);
				for (File f : directory.listFiles())
					f.delete();
			} else if (args.contains("/*.")) {
				String dir = args.substring(0, args.length() - 3);
				File directory = new File(path + dir);
				String format = args.split("/*.")[1];
				for (File f : directory.listFiles())
					if (f.getName().endsWith("." + format))
						f.delete();
			} else
				(new File(path + args)).delete();
			break;
		case "echo":
			String[] pcs = args.split(" ");
			if (pcs[0].compareTo("--target=file") == 0) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path + pcs[1])));
				writer.write(pcs[2].replaceAll("%0", " ").replaceAll("%1", "\n"));
				writer.close();
			} else if (pcs[0].compareTo("--target=console") == 0)
				System.out.println(args.replace(pcs[0] + " ", ""));
			break;
		case "write_new":
			pcs = args.split(" ");
			File f = new File(path + pcs[0]);
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(pcs[1].replaceAll("%0", " ").replaceAll("%1", "\n"));
			writer.close();
			break;
		case "new":
			pcs = args.split(" ");
			if (pcs[0].compareTo("--type=folder") == 0)
				(new File(path + pcs[1])).mkdir();
			else if (pcs[0].compareTo("--type=file") == 0)
				(new File(path + pcs[1])).createNewFile();
			break;
		}
	}

	public String getSource() {
		return source;
	}

	public String getUpdateFunctionName() {
		return updateFunctionName;
	}
}