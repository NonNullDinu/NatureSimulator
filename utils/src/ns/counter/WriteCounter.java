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

package ns.counter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

class WriteCounter {
	private static int counter;
	private static int lineCounter;
	private static long mem;

	public static void main(String[] args) {
		int totalCounter = 0, totalLineCounter = 0;
		File sourceFolder = new File("../src");
		count(sourceFolder);
		System.out.println("src = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;

		sourceFolder = new File("../utils");
		count(sourceFolder);
		System.out.println("utils = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;

		sourceFolder = new File("../ns.core");
		count(sourceFolder);
		System.out.println("ns.core = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;

		sourceFolder = new File("../ns.patch");
		count(sourceFolder);
		System.out.println("ns.patch = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;

		sourceFolder = new File("../gameData/shaders");
		count(sourceFolder);
		System.out.println("shaders = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;

		System.out.println("total = " + totalCounter + "(" + totalLineCounter + " lines), " + mem + " bytes, or "
				+ Math.floor((double) mem / 10.24) / 100.0 + " kilobytes");
	}

	private static void count(File file) {
		if (file.isDirectory())
			for (File f : Objects.requireNonNull(file.listFiles())) {
				count(f);
			}
		else {
			try {
				mem += file.length();
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = reader.readLine()) != null) {
					counter += (line.length() + 1);
					lineCounter++;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}