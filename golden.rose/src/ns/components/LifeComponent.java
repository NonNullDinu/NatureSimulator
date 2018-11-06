package ns.components;

import ns.display.DisplayManager;

public class LifeComponent implements IComponent {
	final float totalLifespan;
	private float lifespanRemaining;
	private boolean dead = false;

	LifeComponent(float totalLifespan) {
		this.totalLifespan = totalLifespan;
		this.lifespanRemaining = totalLifespan;
	}

	public void update() {
		lifespanRemaining -= DisplayManager.getInGameTimeSeconds();
		if (lifespanRemaining <= 0f) {
			dead = true;
		}
	}

	public boolean isDead() {
		return dead;
	}

	float getRemainingLifespan() {
		return lifespanRemaining;
	}

	@Override
	public IComponent copy() {
		return new LifeComponent(totalLifespan);
	}

	public void setDead(boolean b) {
		dead = b;
	}
}