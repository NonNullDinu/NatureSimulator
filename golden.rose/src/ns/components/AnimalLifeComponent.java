/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.components;

import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.genetics.DNA;
import org.lwjgl.util.vector.Vector2f;

public class AnimalLifeComponent extends LifeComponent {
	private static final long serialVersionUID = 6330580179418676814L;
	float REPR_TIME;
	private float hungerPoints;
	private int cooldown;
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

	public AnimalLifeComponent withDNA(@SuppressWarnings("exports") DNA dna) {
		this.dna = dna;
		return this;
	}

	@SuppressWarnings("exports")
	public DNA getDna() {
		return dna;
	}

	public IComponent copy() {
		return new AnimalLifeComponent(super.totalLifespan).withReprTime(REPR_TIME);
	}

	@Override
	public boolean isWithinHeightLimits(float y) {
		Vector2f lim = dna.getHeightLimits();
		return y >= lim.x && y <= lim.y;
	}

	public boolean isOffspringCreating() {
		return offspring;
	}

	public void setOffspring(boolean offspring) {
		this.offspring = offspring;
	}
}