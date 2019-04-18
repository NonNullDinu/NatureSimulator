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

import java.io.IOException;
import java.io.OutputStream;

public class Out {
	private PATH location;
	private OutputStream outputStream;

	private Out() {
	}

	public static Out create(String location) {
		return new Out().withLocation(location).create();
	}

	private Out withLocation(String location) {
		return withLocation(new PATH(location));
	}

	private Out withLocation(PATH location) {
		this.location = location;
		return this;
	}

	private Out create() {
		try {
			this.outputStream = location.openOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public void writeVersion(int version) throws IOException {
		this.outputStream.write(version);
	}

	public OutputStream asOutputStream() {
		return outputStream;
	}

	public PATH getLocation() {
		return location;
	}
}