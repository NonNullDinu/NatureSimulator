package ns.mainEngine;

import data.SaveData;
import ns.parallelComputing.ThreadMaster;
import ns.utils.GU;
import ns.world.WorldGenerator;
import ns.worldSave.SaveWorldMaster;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static java.lang.System.getProperty;

/**
 * @version 1.3.2-alpha
 */
class Initializer {
	public static void main(String[] args) {
		processArgs(args);
		if (GU.path == null)
			GU.path = System.getProperty("user.dir") + "/";
		System.setProperty("org.lwjgl.librarypath", GU.path + "lib/natives");
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
						+ "java.runtime.version:" + getProperty("java.runtime.version") + "\n"  //
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
					SaveWorldMaster.save(WorldGenerator.generatedWorld, SaveData.openOutput("save0." + GU.WORLD_SAVE_FILE_FORMAT));
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

		thread = ThreadMaster.createThread(new LoadingScreenThread(), "loading screen thread");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new SecondaryThread(), "secondary thread");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new ThirdThread(), "third thread");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();
	}

	private static int hashCode(Throwable e) {
		String s = e.getLocalizedMessage();
		for (StackTraceElement ste : e.getStackTrace()) {
			s += "\n" + ste.getFileName() + " " + ste.getClassName() + " " + ste.getMethodName() + " " + ste.getModuleName() +
					" " + ste.getLineNumber();
		}
		return Arrays.hashCode(s.getBytes());
	}

	private static void processArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-p") || args[i].equals("--path")) {
				i++;
				GU.path = args[i];
			} else System.out.println("Unknown arg:" + args[i]);
		}
	}
}