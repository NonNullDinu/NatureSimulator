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

import ns.utils.GU;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class In {

	private PATH location;
	private InputStream asInputStream;
	private boolean exists;
	private int ver;
	private boolean hasVersion;

	private In() {
	}

	public static In create(String location) {
		return new In().withLocation(location).create();
	}

	public static In create(String location, boolean version) {
		return new In().withLocation(location).withVersion(version).create();
	}

	private In withLocation(String location) {
		return withLocation(new PATH(location));
	}

	private In withLocation(PATH location) {
		this.location = location;
		return this;
	}

	private In withVersion(boolean version) {
		this.hasVersion = version;
		return this;
	}

	private In create() {
		try {
			this.asInputStream = location.openInput();
			exists = true;
		} catch (FileNotFoundException ignored) {
		}
		if (this.exists && this.hasVersion) {
			try {
				this.ver = asInputStream.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
	}

	public PATH getLocation() {
		return location;
	}

	public InputStream asInputStream() throws NullPointerException {
		if (asInputStream != null)
			return asInputStream;
		else {
			System.err.println("File not found: " + location.relativeLocation() + ". Is the path " + GU.path.substring(0, GU.path.length() - 1) + " correct?");
			throw new NullPointerException("File at " + location.relativeLocation() + " could not be found");
		}
	}

	public boolean exists() {
		return exists;
	}

	public int version() {
		return ver;
	}
}
