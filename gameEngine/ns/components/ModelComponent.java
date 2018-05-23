package ns.components;

import ns.openglObjects.VAO;

public class ModelComponent implements IComponent {
	private final VAO model;

	public ModelComponent(VAO model) {
		this.model = model;
	}

	public VAO getModel() {
		return model;
	}
}