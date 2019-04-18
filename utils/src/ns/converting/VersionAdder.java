/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
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