package ns.parallelComputing;

import java.util.HashMap;
import java.util.Map;

public class ThreadMaster {
	private static final Map<String, Thread> threads = new HashMap<>();

	public static Thread getThread(String name) {
		return threads.get(name);
	}

	public static Thread createThread(Runnable runnable, String name) {
		Thread th = new Thread(name, runnable);
		threads.put(name, th);
		return th;
	}
}