package ns.mainEngine;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import ns.parallelComputing.ThreadMaster;
import ns.utils.GU;
import ns.world.WorldGenerator;
import ns.worldSave.SaveWorldMaster;
import res.WritingResource;

/**
 * @version 1.1.8
 */
public class Initializer {
	public static void main(String[] args) {
		UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				String msg = "";
				for (StackTraceElement elem : e.getStackTrace()) {
					// Formatting for use with eclipse
					msg += elem.getModuleName() + "/" + elem.getClassName() + "." + elem.getMethodName() + "("
							+ elem.getFileName() + ":" + elem.getLineNumber() + ")\n	";
				}
				File f = new File("err" + new SimpleDateFormat("hh mm ss dd MM yyyy").format(new Date()) + ".log");
				System.err.println(e.getClass().getName() + "\nStack trace: " + msg);
				try {
					f.createNewFile();
					DataOutputStream dout = new DataOutputStream(new FileOutputStream(f));
					dout.writeUTF(e.getClass().getName() + "\nStack trace: " + msg);
					dout.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					SaveWorldMaster.save(WorldGenerator.generatedWorld,
							new WritingResource("saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT));
				} catch (Throwable thr) {
					msg = "";
					for (StackTraceElement elem : thr.getStackTrace()) {
						msg += elem.getFileName() + ": " + elem.getMethodName() + "(line " + elem.getLineNumber()
								+ ")\n	";
					}
					System.err.print(thr.getClass().getName() + "\nError while saving world:\nAt:" + msg);
					new File("saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT).delete();
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