package ns.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ns.utils.GU;
import res.Resource;

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
		shaders.add(new Resource("res/shaders/standard/vertexShader.glsl"));
		shaders.add(new Resource("res/shaders/standard/fragmentShader.glsl")); // Entity (standard)
		shaders.add(new Resource("res/shaders/terrain/terrainVertex.glsl"));
		shaders.add(new Resource("res/shaders/terrain/terrainFragment.glsl")); // Terrain
		shaders.add(new Resource("res/shaders/water/vertex.glsl"));
		shaders.add(new Resource("res/shaders/water/fragment.glsl")); // Water
		shaders.add(new Resource("res/shaders/guis/guiVertex.glsl"));
		shaders.add(new Resource("res/shaders/guis/guiFragment.glsl")); // GUI
		shaders.add(new Resource("res/shaders/menuDNA/vertexShader.glsl"));
		shaders.add(new Resource("res/shaders/menuDNA/fragmentShader.glsl")); // MenuDNA
//		shaders.add(new Resource("res/shaders/depthFieldBlur/vertex.glsl"));
//		shaders.add(new Resource("res/shaders/depthFieldBlur/fragment.glsl")); // Depth field blur(not using)
		shaders.add(new Resource("res/shaders/colorQuad/quadVertex.glsl"));
		shaders.add(new Resource("res/shaders/colorQuad/quadFragment.glsl")); // Color Quad
		shaders.add(new Resource("res/shaders/blur/fshader.glsl"));
		shaders.add(new Resource("res/shaders/blur/vvshader.glsl"));
		shaders.add(new Resource("res/shaders/blur/vhshader.glsl")); // Gaussian blur
	}

	public static String getSource(String shaderName) {
		return sources.get(shaderName);
	}
}