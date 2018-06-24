package ns.openglWorkers;

import java.util.HashMap;
import java.util.Map;

import ns.customFileFormat.MdlFile;
import ns.openglObjects.VAO;

public class ModelsLibrary {
	private static Map<String, VAO> models = new HashMap<>();

	public static VAO getModel(String name) {
		VAO model = models.get(name);
		if (model == null) {
			model = new MdlFile(name.replace(".obj", ".mdl")).load();
			models.put(name, model);
		}
		return model;
	}
}