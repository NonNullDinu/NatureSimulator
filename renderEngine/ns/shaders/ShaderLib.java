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
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/standard/vertexShader.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/standard/fragmentShader.glsl").create()); // Entity (standard)
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/terrain/terrainVertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/terrain/terrainFragment.glsl").create()); // Terrain
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/water/vertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/water/fragment.glsl").create()); // Water
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/guis/guiVertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/guis/guiFragment.glsl").create()); // GUI
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/menuDNA/vertexShader.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/menuDNA/fragmentShader.glsl").create()); // MenuDNA
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/colorQuad/quadVertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/colorQuad/quadFragment.glsl").create()); // Color Quad
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/blur/fshader.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/blur/vvshader.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("res/shaders/blur/vhshader.glsl").create()); // Gaussian blur
	}

	public static String getSource(String shaderName) {
		return sources.get(shaderName);
	}
}