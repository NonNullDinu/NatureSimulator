package ns.parallelComputing;

import java.util.HashMap;
import java.util.Map;

public class ThreadMaster {
	private static final Map<String, ns.parallelComputing.Thread> threads = new HashMap<>();

	public static ns.parallelComputing.Thread getThread(String name) {
		return threads.get(name);
	}

	public static ns.parallelComputing.Thread createThread(Runnable runnable, String name) {
		ns.parallelComputing.Thread th = new ns.parallelComputing.Thread(name, runnable);
		threads.put(name, th);
		return th;
	}
}