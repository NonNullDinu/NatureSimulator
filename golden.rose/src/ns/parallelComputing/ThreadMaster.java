/*
 * Copyright (C) 2018  Dinu Blanovschi
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