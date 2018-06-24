package ns.mainEngine;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import static java.lang.System.*;

import ns.parallelComputing.ThreadMaster;
import ns.utils.GU;
import ns.world.WorldGenerator;
import ns.worldSave.SaveWorldMaster;
import res.WritingResource;

/**
 * @version 1.3
 */
public class Initializer {
	public static void main(String[] args) {
		GU.path = (args.length == 0 ? System.getProperty("user.dir") : args[0]) + "/";
		System.setProperty("org.lwjgl.librarypath", new File(GU.path + "lib/natives").getAbsolutePath());
		UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				String msg = "";
				for (StackTraceElement elem : e.getStackTrace()) {
					// Formatting for use with eclipse
					msg += elem.getModuleName() + "/" + elem.getClassName() + "." + elem.getMethodName() + "("
							+ elem.getFileName() + ":" + elem.getLineNumber() + ")\n	";
				}
				String props = "sun.desktop:" + getProperty("sun.desktop") + "\n" + "java.specification.version:"
						+ getProperty("java.specification.version") + "\n" + "os.name:" + getProperty("os.name") + "\n"
						+ "java.vm.specification.version:" + getProperty("java.vm.specification.version") + "\n"
						+ "java.runtime.version:" + getProperty("java.runtime.version") + "\n" + "os.version:"
						+ getProperty("os.version") + "\n" + "java.runtime.name:" + getProperty("java.runtime.name")
						+ "\n" + "java.vm.name:" + getProperty("java.vm.name") + "\n" + "java.version:"
						+ getProperty("java.version") + "\n" + "os.arch:" + getProperty("os.arch") + "\n"
						+ "java.vm.version:" + getProperty("java.vm.version") + "\n" + "java.class.version:"
						+ getProperty("java.class.version") + "\n";
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
				if (WorldGenerator.generatedWorld != null)
					try {
						SaveWorldMaster.save(WorldGenerator.generatedWorld, new WritingResource()
								.withLocation(GU.path + "saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT).create());
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
				System.exit(-1);
			}
		};

		ns.parallelComputing.Thread thread;
		thread = ThreadMaster.createThread(new MainGameLoop(), GU.MAIN_THREAD_NAME);
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new SecondaryThread(), "secondary thread");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new ThirdThread(), "third thread");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new LoadingScreenThread(), "loading screen thread");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();
	}
}