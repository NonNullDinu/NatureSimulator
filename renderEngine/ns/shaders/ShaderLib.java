package ns.shaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ns.utils.GU;
import res.Resource;

public class ShaderLib {
	private static final Map<String, String> sources = new HashMap<>();	
	
	public static void loadAll() {
		File shadersPackage = new File("resources/res/shaders");
		for(File shaderPackage : shadersPackage.listFiles()) {
			for(File shaderFile : shaderPackage.listFiles()) {
				Resource resource = new Resource(shaderFile.getPath().replace("resources/", ""));
				BufferedReader reader = GU.open(resource);
				String line, source = "";
				try {
					while((line = reader.readLine()) != null) {
						if (line.startsWith("#Struct_Lib.")) {
							line = StructLib.get(line.replace("#Struct_Lib.", ""));
						}
						source += line + "\n";
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sources.put(resource.getLocation(), source);
			}
		}
	}
	
	public static String getSource(String shaderName) {
		return sources.get(shaderName);
	}
}