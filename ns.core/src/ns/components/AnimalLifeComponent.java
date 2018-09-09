package ns.components;

import ns.entities.Entity;

public class AnimalLifeComponent extends LifeComponent {
	private float hungerPoints;
	private int cooldown;

	public AnimalLifeComponent(float totalLifespan) {
		super(totalLifespan);
		this.hungerPoints = 0;
	}

	public boolean isHungry() {
		return hungerPoints > 20;
	}

	public boolean isStarving() {
		return hungerPoints > 30;
	}

	@Override
	public void update() {
		super.update();
		cooldown--;
	}

	public void eat(Entity food) {
		if (cooldown <= 0) {
			float dif = Math.min(food.getFoodComp().amount, 15f);
			hungerPoints -= dif;
			food.getFoodComp().eat(dif);
			cooldown = 50;
		}
	}
}
