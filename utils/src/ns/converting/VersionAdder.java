package ns.converting;

import java.io.*;

class VersionAdder {

	public static void main(String[] args) throws IOException {
		byte[] buf = new byte[80];
		int len = System.in.read(buf);
		String location = "";
		for (int i = 0; i < len - 1; i++)
			location += (char) buf[i];
		File target = new File("resources/" + location);
		BufferedReader reader = new BufferedReader(new FileReader(target));
		String content = "";
		String line;
		while((line = reader.readLine()) != null)
			content += line + "\n";
		reader.close();
		BufferedWriter writer = new BufferedWriter(new FileWriter(target));
		buf = new byte[3];
		len = System.in.read(buf);
		String ver = "";
		for (int i = 0; i < len - 1; i++)
			ver += (char) buf[i];
		writer.write((int) Byte.parseByte(ver));
		writer.write(content);
		writer.close();
	}
}