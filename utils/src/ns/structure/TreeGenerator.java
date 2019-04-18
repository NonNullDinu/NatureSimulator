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

package ns.structure;

import java.io.File;
import java.util.Objects;

class TreeGenerator {
	public static void main(String[] args) {
		File f = new File("../gameData");
		String s = "<root_tree>\n";
		for (File fl : Objects.requireNonNull(f.listFiles()))
			s = add(fl, s);
		s += "</root_tree>";
		System.out.println(s);
	}

	private static String add(File f, String s) {
		if (f.isDirectory()) {
			s += "<folder name=\"" + f.getName() + "\">";
			for (File fl : Objects.requireNonNull(f.listFiles()))
				s = add(fl, s);
			s += "</folder>";
		} else {
			System.out.println(f.getPath());
			int p = f.getName().indexOf('.');
			s += "<file name=\"" + f.getName().substring(0, p) + "\" extension=\"" + f.getName().substring(p) + "\"/>";
		}
		return s;
	}
}
