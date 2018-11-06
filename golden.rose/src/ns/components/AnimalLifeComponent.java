package ns.components;

import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.genetics.DNA;

public class AnimalLifeComponent extends LifeComponent {
	private float hungerPoints;
	private int cooldown;
	float REPR_TIME;
	private DNA dna;
	private float repr_time;
	private boolean offspring;

	AnimalLifeComponent(float totalLifespan) {
		super(totalLifespan);
		this.hungerPoints = 0;
	}

	AnimalLifeComponent withReprTime(float REPR_TIME) {
		this.REPR_TIME = REPR_TIME;
		return this;
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

		repr_time += DisplayManager.getInGameTimeSeconds();
		if (repr_time >= REPR_TIME) {
			offspring = true;
			repr_time = 0f;
		}
	}

	public void eat(Entity food) {
		if (cooldown <= 0) {
			float dif = Math.min(food.getFoodComp().amount, 15f);
			hungerPoints -= dif;
			food.getFoodComp().eat(dif);
			cooldown = 50;
		}
	}

	public AnimalLifeComponent withDNA(DNA dna) {
		this.dna = dna;
		return this;
	}

	public DNA getDna() {
		return dna;
	}

	public IComponent copy() {
		return new AnimalLifeComponent(super.totalLifespan).withReprTime(REPR_TIME);
	}

	public boolean isOffspringCreating() {
		return offspring;
	}

	public void setOffspring(boolean offspring) {
		this.offspring = offspring;
	}
}