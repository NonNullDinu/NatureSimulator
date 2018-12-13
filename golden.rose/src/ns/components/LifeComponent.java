package ns.components;

import ns.display.DisplayManager;

public abstract class LifeComponent implements IComponent {
	private static final long serialVersionUID = 6321979806232876908L;

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

	public void setDead(boolean b) {
		dead = b;
	}

	float getRemainingLifespan() {
		return lifespanRemaining;
	}

	@Override
	public IComponent copy() {
		return this.getClass() == AnimalLifeComponent.class ? new AnimalLifeComponent(totalLifespan) :
				new PlantLifeComponent(totalLifespan);
	}

	public abstract boolean isWithinHeightLimits(float y);
}