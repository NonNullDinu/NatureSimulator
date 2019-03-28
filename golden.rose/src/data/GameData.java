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

package data;

import ns.utils.GU;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import resources.DirectoryPointer;
import resources.IFolder;
import resources.In;

import java.io.InputStream;
import java.util.Objects;

public class GameData implements IFolder, DirectoryPointer {
	private static GameData folder;

	private GameData(int version, int patch) {
		if (version != 1 || patch != 2) {
			System.err.println("Unable to load resources: game folder version unknown");
			System.exit(-1);
		}
	}

	public static void init() {
		InputStream ins = In.create("gameData/gameData.fd.xml").asInputStream();
		Document doc = GU.getDocument(ins);
		Element documentElement = Objects.requireNonNull(doc).getDocumentElement();
		NodeList nodes = documentElement.getChildNodes();
		int version = 0, patch = 0;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().equals("version")) {
				String arg = node.getAttributes().getNamedItem("v").getNodeValue();
				version = Integer.parseInt(arg.substring(0, 1), 16);
				patch = Integer.parseInt(arg.substring(2, 3), 16);
			}
		}
		folder = new GameData(version, patch);
	}

	public static boolean initialized() {
		return folder != null;
	}

	public static In getResourceAt(String location) {
		return folder._getResourceAt(location);
	}

	public static In getResourceAt(String location, boolean version) {
		return folder._getResourceAt(location, version);
	}

	@Override
	public In _getResourceAt(String location) {
		return In.create("gameData/" + location);
	}

	@Override
	public In _getResourceAt(String location, boolean version) {
		return In.create("gameData/" + location, version);
	}

	@Override
	public String onWindowsLoc() {
		return GU.path + "gameData/";
	}

	@Override
	public String onLinuxLoc() {
		return "/usr/share/ns/gameData/";
	}
}