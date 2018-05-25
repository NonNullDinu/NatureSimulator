package ns.counter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WriteCounter {
	private static int counter;
	private static int lineCounter;

	public static void main(String[] args) {
		int totalCounter = 0, totalLineCounter = 0;
		File sourceFolder = new File("src");
		count(sourceFolder);
		System.out.println("src = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;
		
		sourceFolder = new File("updater");
		count(sourceFolder);
		System.out.println("updater = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;
		
		sourceFolder = new File("gameEngine");
		count(sourceFolder);
		System.out.println("gameEngine = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;
		
		sourceFolder = new File("renderEngine");
		count(sourceFolder);
		System.out.println("renderEngine = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;
		
		sourceFolder = new File("resources/res/shaders");
		count(sourceFolder);
		System.out.println("shaders = " + counter + "(" + lineCounter + " lines)");
		totalCounter += counter;
		totalLineCounter += lineCounter;
		counter = 0;
		lineCounter = 0;
		
		System.out.println("total = " + totalCounter + "(" + totalLineCounter + " lines)");
	}

	private static void count(File file) {
		if (file.isDirectory())
			for (File f : file.listFiles()) {
				count(f);
			}
		else {
			try {
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