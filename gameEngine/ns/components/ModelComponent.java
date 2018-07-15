package ns.components;

import ns.openglObjects.VAO;

public class ModelComponent implements IComponent {
	private final VAO model;
	private boolean shouldScale = false;

	public ModelComponent(VAO model) {
		this.model = model;
	}

	public VAO getModel() {
		return model;
	}
	
	public ModelComponent shouldScaleTrue() {
		this.shouldScale = true;
		return this;
	}
	
	public boolean shouldScale() {
		return shouldScale;
	}
}