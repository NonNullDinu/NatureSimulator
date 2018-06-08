package ns.openglWorkers;

import java.util.HashMap;
import java.util.Map;

import ns.openglObjects.VAO;
import obj.OBJLoader;

public class ModelsLibrary {
	private static Map<String, VAO> models = new HashMap<>();
	
	public static void cleanUp() {
		for(VAO vao : models.values()) {
			vao.cleanUp();
		}
	}

	public static VAO getModel(String name) {
		VAO model = models.get(name);
		if(model == null) {
			model = OBJLoader.loadObj(name);
			models.put(name, model);
		}
		return model;
	}
}