package online;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("192.168.1.13", 2500);
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader vreader = new BufferedReader(new FileReader("version"));
		writer.println("version:" + vreader.readLine());
		vreader.close();
		String path = System.getProperty("user.dir") + "/";

		String line = reader.readLine();
		if (line.compareTo("NO_UPDATES") == 0) {
			System.out.println("Nothing new to update");
			writer.close();
			reader.close();
			socket.close();
		} else {
			exec(line, path);
			while((line = reader.readLine()).compareTo("FINISHED") != 0) {
				exec(line, path);
			}
		}
	}

	private static void exec(String line, String path) throws IOException {
		if(line.startsWith("createfolder "))
			(new File(path + line.split(" ")[1])).mkdirs();
		else if(line.startsWith("createfile ")) {
			(new File(path + line.split(" ")[1])).createNewFile();
		}
		else if(line.startsWith("writetofile ")) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(line.split(" ")[1])));
			writer.write(line.split(" ")[2].replaceAll("%20", " ").replaceAll("//\\nln", "\n"));
			writer.close();
		}
	}
}