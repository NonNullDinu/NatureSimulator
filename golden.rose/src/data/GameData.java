package data;

import ns.utils.GU;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import resources.IFolder;
import resources.In;

import java.io.InputStream;
import java.util.Objects;

public class GameData implements IFolder {
	private static GameData folder;

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
				version = Integer.parseInt(arg.substring(0, 1));
				patch = Integer.parseInt(arg.substring(2, 3));
			}
		}
		folder = new GameData(version, patch);

	}

	private GameData(int version, int patch) {
		if (version != 1 || patch != 2) {
			System.err.println("Unable to load resources: game folder version unknown");
			System.exit(-1);
		}
	}

	public static boolean initialized() {
		return folder != null;
	}

	@Override
	public In _getResourceAt(String location) {
		return In.create("gameData/" + location);
	}

	public static In getResourceAt(String location) {
		return folder._getResourceAt(location);
	}

	@Override
	public In _getResourceAt(String location, boolean version) {
		return In.create("gameData/" + location, version);
	}

	public static In getResourceAt(String location, boolean version) {
		return folder._getResourceAt(location, version);
	}
}