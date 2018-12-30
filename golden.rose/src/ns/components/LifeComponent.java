/*
 * Copyright (C) 2018  Dinu Blanovschi
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