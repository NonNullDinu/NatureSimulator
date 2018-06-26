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
		shaders.add(new Resource().withVersion(false).withLocation("shaders/standard/vertexShader.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/standard/fragmentShader.glsl").create()); // Entity (standard)
		shaders.add(new Resource().withVersion(false).withLocation("shaders/terrain/terrainVertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/terrain/terrainFragment.glsl").create()); // Terrain
		shaders.add(new Resource().withVersion(false).withLocation("shaders/water/vertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/water/fragment.glsl").create()); // Water
		shaders.add(new Resource().withVersion(false).withLocation("shaders/guis/guiVertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/guis/guiFragment.glsl").create()); // GUI
		shaders.add(new Resource().withVersion(false).withLocation("shaders/menuDNA/vertexShader.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/menuDNA/fragmentShader.glsl").create()); // MenuDNA
		shaders.add(new Resource().withVersion(false).withLocation("shaders/quad/quadVertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/quad/quadFragment.glsl").create()); // Color Quad
		shaders.add(new Resource().withVersion(false).withLocation("shaders/blur/fshader.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/blur/vvshader.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/blur/vhshader.glsl").create()); // Gaussian blur
		shaders.add(new Resource().withVersion(false).withLocation("shaders/font/fontVertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/font/fontFragment.glsl").create()); // Font
		shaders.add(new Resource().withVersion(false).withLocation("shaders/river/riverVertex.glsl").create());
		shaders.add(new Resource().withVersion(false).withLocation("shaders/river/riverFragment.glsl").create()); // River
	}

	public static String getSource(String shaderName) {
		return sources.get(shaderName);
	}
}