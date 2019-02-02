/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.mainEngine;

import data.SaveData;
import ns.parallelComputing.ThreadMaster;
import ns.utils.GU;
import ns.world.WorldGenerator;
import ns.worldLoad.WorldLoadMaster;
import ns.worldSave.SaveWorldMaster;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static java.lang.System.getProperty;

/**
 * @version 1.3.3-alpha
 */
class Initializer {
	private static String NATIVE_LIB_PATH;

	public static void main(String[] args) {
		processArgs(args);
		if (GU.path == null)
			GU.path = System.getProperty("user.dir") + "/";
		System.setProperty("org.lwjgl.librarypath", NATIVE_LIB_PATH != null ? NATIVE_LIB_PATH : GU.path + "lib/natives");
		UncaughtExceptionHandler handler = (Thread t, Throwable e) -> {
			String msg = "";
			if (!(e instanceof java.nio.BufferOverflowException)) {
				for (StackTraceElement elem : e.getStackTrace()) {
					msg += elem.getModuleName() + "/" + elem.getClassName() + "." + elem.getMethodName() + "("
							+ elem.getFileName() + ":" + elem.getLineNumber() + ")\n	";
				}
				String props = "sun.desktop:" + getProperty("sun.desktop") + "\n" //
						+ "java.specification.version:" + getProperty("java.specification.version") + "\n"//
						+ "os.name:" + getProperty("os.name") + "\n"//
						+ "java.vm.specification.version:" + getProperty("java.vm.specification.version") + "\n" //
						+ "java.runtime.version:" + getProperty("java.runtime.version") + "\n" //
						+ "os.version:" + getProperty("os.version") + "\n" //
						+ "java.runtime.name:" + getProperty("java.runtime.name") + "\n" //
						+ "java.vm.name:" + getProperty("java.vm.name") + "\n" //
						+ "java.version:" + getProperty("java.version") + "\n" //
						+ "os.arch:" + getProperty("os.arch") + "\n" //
						+ "java.vm.version:" + getProperty("java.vm.version") + "\n" //
						+ "java.class.version:" + getProperty("java.class.version") + "\n";
				File f = new File(
						GU.path + "err" + new SimpleDateFormat("hh mm ss dd MM yyyy").format(new Date()) + ".log");
				System.err.println(e.getClass().getName() + ":\"" + e.getMessage() + "\" in  thread \"" + t.getName()
						+ "\"\nStack trace: " + msg + "\n Proprieties:\n" + props);
				try {
					f.createNewFile();
					DataOutputStream dout = new DataOutputStream(new FileOutputStream(f));
					dout.writeUTF(e.getClass().getName() + ":\"" + e.getMessage() + "\" in  thread \"" + t.getName()
							+ "\"\nStack trace: " + msg + "\n Proprieties:\n" + props);
					dout.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (WorldGenerator.generatedWorld != null)
				try {
					SaveWorldMaster.save(WorldGenerator.generatedWorld,
							SaveData.openOutput("save" + WorldLoadMaster.count + "." + GU.WORLD_SAVE_FILE_FORMAT));
					try (OutputStream savesDirCount = SaveData.openOutput("saves.dir").asOutputStream()) {
						savesDirCount.write(Integer.toString(WorldLoadMaster.count).getBytes());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} catch (Throwable thr) {
					msg = "";
					for (StackTraceElement elem : thr.getStackTrace()) {
						msg += elem.getModuleName() + "/" + elem.getClassName() + "." + elem.getMethodName() + "("
								+ elem.getFileName() + ":" + elem.getLineNumber() + ")\n	";
					}
					System.err.print(thr.getClass().getName() + ":\"" + thr.getMessage()
							+ "\"\nError while saving world:\nAt:" + msg);
					new File(GU.path + "saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT).delete();
				}
			System.exit(hashCode(e));
		};

		ns.parallelComputing.Thread thread;

		GU.init();

		thread = ThreadMaster.createThread(new MainGameLoop(), GU.MAIN_THREAD_NAME);
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new LoadingScreenThread(), "l");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new SecondaryThread(), "s");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new ThirdThread(), "t");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();
	}

	private static int hashCode(Throwable e) {
		String s = e.getLocalizedMessage();
		for (StackTraceElement ste : e.getStackTrace()) {
			s += "\n" + ste.getFileName() + " " + ste.getClassName() + " " + ste.getMethodName() + " "
					+ ste.getModuleName() + " " + ste.getLineNumber();
		}
		return Arrays.hashCode(s.getBytes());
	}

	private static void processArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-p") || args[i].equals("--path")) {
				i++;
				GU.path = args[i] + (args[i].endsWith("/") ? "" : "/");
			} else if (args[i].equals("-l") || args[i].equals("--lib-path")) {
				i++;
				NATIVE_LIB_PATH = args[i] + (args[i].endsWith("/") ? "" : "/");
			} else
				System.out.println("Unknown arg:" + args[i]);
		}
	}
}