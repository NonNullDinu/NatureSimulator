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

package lang;

import tokens.Value;
import variables.DATA_TYPE;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public enum METHOD {
	EXIT((Value[] values) -> {

		System.out.print("\n\nExecution finished");
		System.exit(values.length == 0 ? 0 : values[0].vi);
		return 0;
	}),

	WRITE_TO((Value[] values) -> {
		if (values.length != 2) {
			System.err.println("2 arguments needed for write_to");
			System.exit(2);
		}
		String s = values[0].vs;
		try (FileOutputStream fout = new FileOutputStream(s)) {
			fout.write(values[1].vbs);
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}),

	DEBUG_PRINT((Value[] values) -> {
		System.out.print("Debug: ");
		for (int i = 0; i < values.length; i++)
			System.out.print(values[i].vi);
		System.out.println();
		return 0;
	}),

	CONTENT_OF_FILE_EQUALS((Value[] values) -> {
		String name = values[0].vs.substring(1);
		String loc = values[0].vs.substring(0, 1);
		if (loc.equals("n")) {
			String path = "/usr/share/ns/" + name;
			String content;
			try (FileInputStream fin = new FileInputStream(path)) {
				content = new String(fin.readAllBytes());
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
			return content.equals(values[1].type == DATA_TYPE.BYTE_STREAM ? new String(values[1].vbs) : values[1].vs) ? 1 : 0;
		} else if (loc.equals("j")) {
			String path = "/usr/share/java/ns/" + name;
			String content;
			try (FileInputStream fin = new FileInputStream(path)) {
				content = new String(fin.readAllBytes());
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
			return content.equals(values[1].type == DATA_TYPE.BYTE_STREAM ? new String(values[1].vbs) : values[1].vs) ? 1 : 0;
		}
		return -1;
	});
	private CALLBACK callback;

	METHOD(CALLBACK callback) {
		this.callback = callback;
	}

	public int call(Value[] values) {
		return callback.call(values);
	}
}
