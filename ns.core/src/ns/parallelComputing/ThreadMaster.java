package ns.parallelComputing;

import ns.parallelComputing.Thread.ThreadState;

import java.util.HashMap;
import java.util.Map;

public class ThreadMaster {
	private static Map<String, Thread> threads = new HashMap<>();

	public static Thread getThread(String name) {
		return threads.get(name);
	}

	public static Thread createThread(Runnable runnable, String name) {
		Thread th = new Thread(name, runnable);
		threads.put(name, th);
		return th;
	}

	public static void checkpoint(Thread thread) {
		boolean finished = true;
		for (Thread t : threads.values()) {
			if (!thread.equals(t)) {
				finished = finished && t.state() != ThreadState.RUNNING;
			}
		}
		if (!finished) {
			thread._stop();
		} else {
			for (Thread th : threads.values()) {
				if (th.state() == ThreadState.WAITING)
					th._resume();
			}
		}
	}
}