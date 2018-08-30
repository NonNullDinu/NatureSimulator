package install;

import javax.swing.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Installer {
	private static final String RUN_COMMAND = "java -jar NatureSimulator.jar -p %s";

	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setFileFilter(null);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Install directory");
		chooser.showOpenDialog(null);
		File installDir = chooser.getSelectedFile();
		while (installDir == null) {
			chooser.showOpenDialog(null);
			installDir = chooser.getSelectedFile();
		}
		installDir.mkdirs();
		if (!(installDir.canExecute() && installDir.canWrite()
				&& installDir.canRead()))
			throw new IllegalStateException(installDir.getAbsolutePath() + " can not be used to install");
		File executable = new File(installDir.getAbsolutePath() + "/NatureSimulator.jar");
		new File(installDir.getPath() + "/saveData").mkdir();
		writeExecutable(executable);
		writeLibFolder(new File(installDir.getAbsolutePath() + "/lib"));
		writeLicense(new File(installDir.getAbsolutePath() + "/LICENSE"));
		writeVersion(new File(installDir.getAbsolutePath() + "/version"));
		try {
			writeResources(installDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (System.getProperty("os.name").equals("Linux")) {
			String[] yn = new String[]{"Yes", "No"};
			int ans = JOptionPane.showOptionDialog(null, "Create menu shortcut?", "Create shortcut",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, yn, null);
			if (ans == 0) {
				writeShortcut("NatureSimulator.desktop", installDir);
			} else {
				writeRun(new File(installDir.getAbsolutePath() + "/run.sh"), installDir);
			}
			makens_installDir(installDir, new File(System.getProperty("user.home") + "/.ns-install"));
		} else if (System.getProperty("os.name").equals("Windows")) {
			System.out.println("Vesions for operating systems other than Linux may or may not work as intended");
			String[] yn = new String[]{"Yes", "No"};
			int ans = JOptionPane.showOptionDialog(null, "Create desktop shortcut?", "Create shortcut",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, yn, null);
			if (ans == 0) {
				// TODO Change the windows .bat file with a windows .lnk file
				writeRun(new File(System.getProperty("user.dir") + "/Desktop/Nature Simulator.bat"), installDir);
			}
			writeRun(new File(installDir.getAbsolutePath() + "/run.bat"), installDir);
		} else
			System.err.println("System not recognized");
		System.out.println("Installation complete!");
		System.exit(0);
	}

	private static void writeVersion(File file) {
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write("1.3.2-alpha".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void makens_installDir(File installDir, File dir) {
		dir.mkdir();
		File target_dir = new File(dir.getAbsolutePath() + "/target-dir");
		try {
			if (!target_dir.exists())
				target_dir.createNewFile();
			FileOutputStream fos = new FileOutputStream(target_dir);
			fos.write(installDir.getAbsolutePath().getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File update_sh = new File(dir.getAbsolutePath() + "/update.sh");
		try {
			if (!update_sh.exists())
				update_sh.createNewFile();
			FileOutputStream fos = new FileOutputStream(update_sh);
			fos.write(("#!bin/bash\n" +
					"cd \"$(cat ~/.ns-install/target-dir)\"\n" +
					"wget -O update.sh https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/updates/latestUpdate.sh \n" +
					"sh update.sh && rm update.sh").getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeRun(File file, File installDir) {
		try {
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write((String.format(RUN_COMMAND, installDir.getAbsolutePath())).getBytes());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeLibFolder(File file) {
		file.mkdir();
		File current;
		if (System.getProperty("os.name").equals("Windows")) {
			System.out.println("Windows detected. Writing windows natives");
			current = new File(file.getAbsolutePath() + "/natives/jinput-dx8_64.dll");
			current.getParentFile().mkdir();
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/jinput-dx8.dll");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/jinput-raw.dll");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/jinput-raw_64.dll");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/lwjgl.dll");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/lwjgl64.dll");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/OpenAL32.dll");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/OpenAL64.dll");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (System.getProperty("os.name").equals("Linux")) {
			System.out.println("Linux detected. Writing linux natives");
			current = new File(file.getAbsolutePath() + "/natives/libjinput-linux.so");
			current.getParentFile().mkdir();
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/libjinput-linux64.so");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/liblwjgl.so");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/liblwjgl64.so");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/libopenal.so");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			current = new File(file.getAbsolutePath() + "/natives/libopenal64.so");
			try {
				current.createNewFile();
				InputStream fin = Installer.class.getResourceAsStream("lib/natives/" + current.getName());
				FileOutputStream fout = new FileOutputStream(current);
				byte[] input = fin.readAllBytes();
				fout.write(input);
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void writeShortcut(String fileName, File installDir) {
		try {
			File f = new File(System.getProperty("user.home") + "/.local/share/applications/" + fileName);
			f.createNewFile();
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));
			wr.write("[Desktop Entry]\nType=Application\nExec=java -jar " + installDir.getAbsolutePath()
					+ "/NatureSimulator.jar "
					+ installDir.getAbsolutePath() + "\nEncoding=UTF-8\nName=Nature Simulator\nIcon=" + installDir.getAbsolutePath() + "/gameData/textures/ns_icon.png");
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeExecutable(File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			InputStream ins = Installer.class.getResourceAsStream("NS_Exec.jar");
			byte[] input = ins.readAllBytes();
			out.write(input);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeLicense(File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(" * Copyright (c) 2002-2007 Lightweight Java Game Library Project\n" +
					" * All rights reserved.\n" +
					" *\n" +
					" * Redistribution and use in source and binary forms, with or without\n" +
					" * modification, are permitted provided that the following conditions are\n" +
					" * met:\n" +
					" *\n" +
					" * * Redistributions of source code must retain the above copyright\n" +
					" *   notice, this list of conditions and the following disclaimer.\n" +
					" *\n" +
					" * * Redistributions in binary form must reproduce the above copyright\n" +
					" *   notice, this list of conditions and the following disclaimer in the\n" +
					" *   documentation and/or other materials provided with the distribution.\n" +
					" *\n" +
					" * * Neither the name of 'Light Weight Java Game Library' nor the names of\n" +
					" *   its contributors may be used to endorse or promote products derived\n" +
					" *   from this software without specific prior written permission.\n" +
					" *\n" +
					" * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS\n" +
					" * \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED\n" +
					" * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR\n" +
					" * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR\n" +
					" * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,\n" +
					" * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,\n" +
					" * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR\n" +
					" * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF\n" +
					" * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
					" * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\n" +
					" * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
					" \n" +
					"                  GNU LESSER GENERAL PUBLIC LICENSE\n" +
					"                       Version 3, 29 June 2007\n" +
					"\n" +
					" Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>\n" +
					" Everyone is permitted to copy and distribute verbatim copies\n" +
					" of this license document, but changing it is not allowed.\n" +
					"\n" +
					"\n" +
					"  This version of the GNU Lesser General Public License incorporates\n" +
					"the terms and conditions of version 3 of the GNU General Public\n" +
					"License, supplemented by the additional permissions listed below.\n" +
					"\n" +
					"  0. Additional Definitions.\n" +
					"\n" +
					"  As used herein, \"this License\" refers to version 3 of the GNU Lesser\n" +
					"General Public License, and the \"GNU GPL\" refers to version 3 of the GNU\n" +
					"General Public License.\n" +
					"\n" +
					"  \"The Library\" refers to a covered work governed by this License,\n" +
					"other than an Application or a Combined Work as defined below.\n" +
					"\n" +
					"  An \"Application\" is any work that makes use of an interface provided\n" +
					"by the Library, but which is not otherwise based on the Library.\n" +
					"Defining a subclass of a class defined by the Library is deemed a mode\n" +
					"of using an interface provided by the Library.\n" +
					"\n" +
					"  A \"Combined Work\" is a work produced by combining or linking an\n" +
					"Application with the Library.  The particular version of the Library\n" +
					"with which the Combined Work was made is also called the \"Linked\n" +
					"Version\".\n" +
					"\n" +
					"  The \"Minimal Corresponding Source\" for a Combined Work means the\n" +
					"Corresponding Source for the Combined Work, excluding any source code\n" +
					"for portions of the Combined Work that, considered in isolation, are\n" +
					"based on the Application, and not on the Linked Version.\n" +
					"\n" +
					"  The \"Corresponding Application Code\" for a Combined Work means the\n" +
					"object code and/or source code for the Application, including any data\n" +
					"and utility programs needed for reproducing the Combined Work from the\n" +
					"Application, but excluding the System Libraries of the Combined Work.\n" +
					"\n" +
					"  1. Exception to Section 3 of the GNU GPL.\n" +
					"\n" +
					"  You may convey a covered work under sections 3 and 4 of this License\n" +
					"without being bound by section 3 of the GNU GPL.\n" +
					"\n" +
					"  2. Conveying Modified Versions.\n" +
					"\n" +
					"  If you modify a copy of the Library, and, in your modifications, a\n" +
					"facility refers to a function or data to be supplied by an Application\n" +
					"that uses the facility (other than as an argument passed when the\n" +
					"facility is invoked), then you may convey a copy of the modified\n" +
					"version:\n" +
					"\n" +
					"   a) under this License, provided that you make a good faith effort to\n" +
					"   ensure that, in the event an Application does not supply the\n" +
					"   function or data, the facility still operates, and performs\n" +
					"   whatever part of its purpose remains meaningful, or\n" +
					"\n" +
					"   b) under the GNU GPL, with none of the additional permissions of\n" +
					"   this License applicable to that copy.\n" +
					"\n" +
					"  3. Object Code Incorporating Material from Library Header Files.\n" +
					"\n" +
					"  The object code form of an Application may incorporate material from\n" +
					"a header file that is part of the Library.  You may convey such object\n" +
					"code under terms of your choice, provided that, if the incorporated\n" +
					"material is not limited to numerical parameters, data structure\n" +
					"layouts and accessors, or small macros, inline functions and templates\n" +
					"(ten or fewer lines in length), you do both of the following:\n" +
					"\n" +
					"   a) Give prominent notice with each copy of the object code that the\n" +
					"   Library is used in it and that the Library and its use are\n" +
					"   covered by this License.\n" +
					"\n" +
					"   b) Accompany the object code with a copy of the GNU GPL and this license\n" +
					"   document.\n" +
					"\n" +
					"  4. Combined Works.\n" +
					"\n" +
					"  You may convey a Combined Work under terms of your choice that,\n" +
					"taken together, effectively do not restrict modification of the\n" +
					"portions of the Library contained in the Combined Work and reverse\n" +
					"engineering for debugging such modifications, if you also do each of\n" +
					"the following:\n" +
					"\n" +
					"   a) Give prominent notice with each copy of the Combined Work that\n" +
					"   the Library is used in it and that the Library and its use are\n" +
					"   covered by this License.\n" +
					"\n" +
					"   b) Accompany the Combined Work with a copy of the GNU GPL and this license\n" +
					"   document.\n" +
					"\n" +
					"   c) For a Combined Work that displays copyright notices during\n" +
					"   execution, include the copyright notice for the Library among\n" +
					"   these notices, as well as a reference directing the user to the\n" +
					"   copies of the GNU GPL and this license document.\n" +
					"\n" +
					"   d) Do one of the following:\n" +
					"\n" +
					"       0) Convey the Minimal Corresponding Source under the terms of this\n" +
					"       License, and the Corresponding Application Code in a form\n" +
					"       suitable for, and under terms that permit, the user to\n" +
					"       recombine or relink the Application with a modified version of\n" +
					"       the Linked Version to produce a modified Combined Work, in the\n" +
					"       manner specified by section 6 of the GNU GPL for conveying\n" +
					"       Corresponding Source.\n" +
					"\n" +
					"       1) Use a suitable shared library mechanism for linking with the\n" +
					"       Library.  A suitable mechanism is one that (a) uses at run time\n" +
					"       a copy of the Library already present on the user's computer\n" +
					"       system, and (b) will operate properly with a modified version\n" +
					"       of the Library that is interface-compatible with the Linked\n" +
					"       Version.\n" +
					"\n" +
					"   e) Provide Installation Information, but only if you would otherwise\n" +
					"   be required to provide such information under section 6 of the\n" +
					"   GNU GPL, and only to the extent that such information is\n" +
					"   necessary to install and execute a modified version of the\n" +
					"   Combined Work produced by recombining or relinking the\n" +
					"   Application with a modified version of the Linked Version. (If\n" +
					"   you use option 4d0, the Installation Information must accompany\n" +
					"   the Minimal Corresponding Source and Corresponding Application\n" +
					"   Code. If you use option 4d1, you must provide the Installation\n" +
					"   Information in the manner specified by section 6 of the GNU GPL\n" +
					"   for conveying Corresponding Source.)\n" +
					"\n" +
					"  5. Combined Libraries.\n" +
					"\n" +
					"  You may place library facilities that are a work based on the\n" +
					"Library side by side in a single library together with other library\n" +
					"facilities that are not Applications and are not covered by this\n" +
					"License, and convey such a combined library under terms of your\n" +
					"choice, if you do both of the following:\n" +
					"\n" +
					"   a) Accompany the combined library with a copy of the same work based\n" +
					"   on the Library, uncombined with any other library facilities,\n" +
					"   conveyed under the terms of this License.\n" +
					"\n" +
					"   b) Give prominent notice with the combined library that part of it\n" +
					"   is a work based on the Library, and explaining where to find the\n" +
					"   accompanying uncombined form of the same work.\n" +
					"\n" +
					"  6. Revised Versions of the GNU Lesser General Public License.\n" +
					"\n" +
					"  The Free Software Foundation may publish revised and/or new versions\n" +
					"of the GNU Lesser General Public License from time to time. Such new\n" +
					"versions will be similar in spirit to the present version, but may\n" +
					"differ in detail to address new problems or concerns.\n" +
					"\n" +
					"  Each version is given a distinguishing version number. If the\n" +
					"Library as you received it specifies that a certain numbered version\n" +
					"of the GNU Lesser General Public License \"or any later version\"\n" +
					"applies to it, you have the option of following the terms and\n" +
					"conditions either of that published version or of any later version\n" +
					"published by the Free Software Foundation. If the Library as you\n" +
					"received it does not specify a version number of the GNU Lesser\n" +
					"General Public License, you may choose any version of the GNU Lesser\n" +
					"General Public License ever published by the Free Software Foundation.\n" +
					"\n" +
					"  If the Library as you received it specifies that a proxy can decide\n" +
					"whether future versions of the GNU Lesser General Public License shall\n" +
					"apply, that proxy's public statement of acceptance of any version is\n" +
					"permanent authorization for you to choose that version for the\n" +
					"Library.\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeResources(File file) throws IOException {
		// Config
		File f = new File(file.getAbsolutePath() + "/gameData/config");
		f.mkdirs();

		f = new File(file.getAbsolutePath() + "/gameData/config/gameConfiguration.config");
		f.createNewFile();
		String[] yn = new String[]{"Yes", "No"};
		int ans = JOptionPane.showOptionDialog(null, "Make full screen?", "Fullscreen", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, yn, null);
		FileOutputStream stream = new FileOutputStream(
				file.getAbsolutePath() + "/gameData/config/gameConfiguration.config");
		if (ans == 0) {
			stream.write(17);
			stream.write(0);
		} else {
			stream.write(16);
			stream.write(0);
		}
		stream.close();
		try {
			File folder = new File(file.getAbsolutePath() + "/gameData");
			if (!folder.exists()) {
				folder.mkdir();
			}
			ZipInputStream zis = new ZipInputStream(Installer.class.getResourceAsStream("gameData.zip"));
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(folder.getAbsolutePath() + "/" + fileName);
				newFile.getParentFile().mkdirs();
				if (fileName.endsWith("/"))
					newFile.mkdir();
				else {
					FileOutputStream fos = new FileOutputStream(newFile);
					fos.write(zis.readAllBytes());
					fos.close();
				}
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}