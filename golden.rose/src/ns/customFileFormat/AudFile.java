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

package ns.customFileFormat;

import data.GameData;
import ns.exceptions.LoadingException;
import ns.openALObjects.Buffer;
import ns.utils.GU;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AudFile implements File {
	private final String location;

	public AudFile(String location) {
		this.location = location;
	}

	@Override
	public Buffer load() throws LoadingException {
		int id = AL10.alGenBuffers();
		try (BufferedReader reader = GU.open(GameData.getResourceAt(location))) {
			String line;
			line = reader.readLine();
			String[] pts = line.split(" ");
			int format = Integer.parseInt(pts[0]);
			int size = Integer.parseInt(pts[1]);
			ByteBuffer data = BufferUtils.createByteBuffer(size);
			int freq = Integer.parseInt(pts[2]);
			line = reader.readLine();
			pts = line.split(" ");
			for (int idx = 0; idx < size; idx++)
				data.put(Byte.parseByte(pts[idx]));
			data.flip();
			AL10.alBufferData(id, format, data, freq);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return new Buffer(id);
	}
}