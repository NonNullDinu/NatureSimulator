package ns.openglWorkers;

import ns.customFileFormat.MdlFile;
import ns.openglObjects.VAO;
import obj.OBJLoader;

import java.util.HashMap;
import java.util.Map;

public class ModelsLibrary {
	private static final Map<String, VAO> models = new HashMap<>();

	public static VAO getModel(String name) {
		VAO model = models.get(name);
		if (model == null) {
			if (name.endsWith(".mdl"))
				model = new MdlFile(name).load();
			else
				model = OBJLoader.loadObj(name);
			models.put(name, model);
		}
		return model;
	}
}