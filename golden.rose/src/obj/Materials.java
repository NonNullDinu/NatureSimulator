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

package obj;

import ns.utils.GU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import resources.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Materials extends ArrayList<Material> {
	private static final long serialVersionUID = -4711987157168197781L;

	public Materials(In resource) {
		this(GU.open(resource));
	}


	public Materials(BufferedReader reader) {
		String line;
		Material current = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("newmtl")) {
					if (current != null) {
						add(current);
					}
					current = new Material((byte) size());
					current.setName(line.split(" ")[1]);
				} else if (line.startsWith("Kd")) {
					String[] pcs = line.split(" ");
					Objects.requireNonNull(current).setColor(
							new Vector3f(Float.parseFloat(pcs[1]), Float.parseFloat(pcs[2]), Float.parseFloat(pcs[3])));
				} else if (line.startsWith("Ka")) {
					String[] pcs = line.split(" ");
					Objects.requireNonNull(current).setIndicators(
							new Vector3f(Float.parseFloat(pcs[1]), Float.parseFloat(pcs[2]), Float.parseFloat(pcs[3])));
				} else if (line.startsWith("d")) {
					String[] pcs = line.split(" ");
					Objects.requireNonNull(current).setData(new Vector4f(Float.valueOf(pcs[1]), 0, 0, 0));
				}
			}
			add(current);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Material get(String name) {
		for (Material m : this) {
			if (m.getName().compareTo(name) == 0)
				return m;
		}
		return null;
	}

	public List<Byte> asByteMaterials() {
		List<Byte> bytes = new ArrayList<>();
		for (Material m : this) {
			bytes.addAll(m.getBytes());
		}
		return bytes;
	}
}
