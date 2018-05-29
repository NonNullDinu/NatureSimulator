package ns.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ns.utils.GU;
import res.Resource;

public class StructLib {
	private static Map<String, String> structs = new HashMap<>();

	public static void load(Resource resource) {
		BufferedReader reader = GU.open(resource);
		try {
			String line;
			String currentStructName = null, structBody = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("}")) {
					structBody += "};";
					structs.put(currentStructName, structBody);
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