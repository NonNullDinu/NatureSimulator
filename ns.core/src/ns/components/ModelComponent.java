package ns.components;

import ns.openglObjects.VAO;

public class ModelComponent implements IComponent {
	public boolean heightStop = false;
	public float stopMovementHeight;
	private transient VAO model;
	private boolean shouldScale = false;

	ModelComponent(VAO model) {
		this.model = model;
	}

	public VAO getModel() {
		return model;
	}

	ModelComponent shouldScaleTrue() {
		this.shouldScale = true;
		return this;
	}

	ModelComponent useHeightStopMovement(float heightStop) {
		this.stopMovementHeight = heightStop;
		this.heightStop = true;
		return this;
	}
	
	public boolean shouldScale() {
		return shouldScale;
	}

	@Override
	public IComponent copy() {
		return heightStop ? new ModelComponent(model).useHeightStopMovement(stopMovementHeight) :
				new ModelComponent(model);
	}

	public void setModel(VAO model) {
		this.model = model;
	}
}