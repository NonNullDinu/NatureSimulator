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

public class FoodComponent implements IComponent {
	private static final long serialVersionUID = -9048230879388446849L;

	float amount;

	FoodComponent(float amount) {
		this.amount = amount;
	}

	void eat(float val) {
		amount -= val;
//		boolean toRemove = false;
//		if (val == 0f)
//			toRemove = true;
	}

	@Override
	public IComponent copy() {
		return new FoodComponent(amount);
	}
}
