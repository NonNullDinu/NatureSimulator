package ns.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.GameData;
import ns.utils.GU;
import resources.Resource;

public class ShaderLib {
	private static final Map<String, String> sources = new HashMap<>();

	public static void loadAll() {
		List<Resource> shaders = new ArrayList<>();
		addDeclarations(shaders);
		for (Resource shaderFile : shaders) {
			BufferedReader reader = GU.open(shaderFile);
			String line, source = "";
			try {
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("#Struct_Lib.")) {
						line = StructLib.get(line.replace("#Struct_Lib.", ""));
					}
					source += line + "\n";
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sources.put(shaderFile.getLocation(), source);
		}
	}

	private static void addDeclarations(List<Resource> shaders) {
		shaders.add(GameData.getResourceAt("shaders/standard/vertexShader.glsl"));
		shaders.add(GameData.getResourceAt("shaders/standard/fragmentShader.glsl")); // Entity (standard)
		shaders.add(GameData.getResourceAt("shaders/terrain/terrainVertex.glsl"));
		shaders.add(GameData.getResourceAt("shaders/terrain/terrainFragment.glsl")); // Terrain
		shaders.add(GameData.getResourceAt("shaders/water/vertex.glsl"));
		shaders.add(GameData.getResourceAt("shaders/water/fragment.glsl")); // Water
		shaders.add(GameData.getResourceAt("shaders/guis/guiVertex.glsl"));
		shaders.add(GameData.getResourceAt("shaders/guis/guiFragment.glsl")); // GUI
		shaders.add(GameData.getResourceAt("shaders/menuDNA/vertexShader.glsl"));
		shaders.add(GameData.getResourceAt("shaders/menuDNA/fragmentShader.glsl")); // MenuDNA
		shaders.add(GameData.getResourceAt("shaders/quad/quadVertex.glsl"));
		shaders.add(GameData.getResourceAt("shaders/quad/quadFragment.glsl")); // Color Quad
		shaders.add(GameData.getResourceAt("shaders/blur/fshader.glsl"));
		shaders.add(GameData.getResourceAt("shaders/blur/vvshader.glsl"));
		shaders.add(GameData.getResourceAt("shaders/blur/vhshader.glsl")); // Gaussian blur
		shaders.add(GameData.getResourceAt("shaders/font/fontVertex.glsl"));
		shaders.add(GameData.getResourceAt("shaders/font/fontFragment.glsl")); // Font
		shaders.add(GameData.getResourceAt("shaders/river/riverVertex.glsl"));
		shaders.add(GameData.getResourceAt("shaders/river/riverFragment.glsl")); // River
	}

	public static String getSource(String shaderName) {
		return sources.get(shaderName);
	}
}