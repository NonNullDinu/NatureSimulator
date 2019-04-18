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

package ns.genetics;

import ns.utils.GU;

final class GeneticsConstants {
	static final double mutateChance = 0.001;
	static final GU.Random geneticsRandom = new GU.Random();

	static {
		geneticsRandom.setSeed(geneticsRandom.genInt(1000000000));
	}

	private GeneticsConstants() {
	}
}