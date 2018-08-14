package ns.shaders;

import ns.utils.GU;
import resources.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StructLib {
	private static Map<String, String> structs = new HashMap<>();

	public static void load(In resource) {
		BufferedReader reader = GU.open(resource);
		try {
			String line;
			String currentStructName = null, structBody = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("}")) {
					structBody += "};";
					structs.put(currentStructName, structBody.replaceAll("\n", ""));
				} else if (line.startsWith("struct ")) {
					currentStructName = line.split(" ")[1];
					if (currentStructName.contains("{")) {
						currentStructName = currentStructName.replace("{", "");
					}
					structBody = line + "\n";
				} else if (!line.equals(""))
					structBody += line + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String get(String name) {
		return structs.get(name);
	}
}