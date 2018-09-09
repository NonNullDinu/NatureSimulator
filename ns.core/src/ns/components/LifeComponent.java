package ns.components;

import ns.display.DisplayManager;

public class LifeComponent implements IComponent {
	private float totalLifespan;
	private float lifespanRemaining;
	private boolean dead = false;

	public LifeComponent(float totalLifespan) {
		this.totalLifespan = totalLifespan;
		this.lifespanRemaining = totalLifespan;
	}

	public void update() {
		lifespanRemaining -= DisplayManager.getFrameTimeSeconds();
		if (lifespanRemaining <= 0f) {
			dead = true;
		}
	}

	public boolean isDead() {
		return dead;
	}

	public float getRemainingLifespan() {
		return lifespanRemaining;
	}
}