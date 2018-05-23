package ns.openglWorkers;

import java.util.HashMap;
import java.util.Map;

import ns.openglObjects.VAO;
import obj.OBJLoader;

public class ModelsLibrary {
	private static Map<String, VAO> models = new HashMap<>();
	
	/**
	 * Intended to be called by the secondary thread, but it's not that much of a difference because @see DataPacking#createVAO
	 */
	public static VAO createEmptyVAO(String objFile) {
		return getModel(objFile);
	}
	
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
