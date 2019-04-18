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

package resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PATH {
	private final _File file;
	private final String relativeLocation;

	public PATH(String location) {
		this(new _File(location));
	}

	public PATH(@SuppressWarnings("exports") _File file) {
		this.file = file;
		this.relativeLocation = file.locGiven;
	}

	public FileInputStream openInput() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public FileOutputStream openOutput() throws IOException {
		if (!file.exists()) {
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return new FileOutputStream(file);
	}

	public String relativeLocation() {
		return relativeLocation;
	}
}
