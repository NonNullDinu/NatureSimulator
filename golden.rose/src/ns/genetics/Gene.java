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

package ns.genetics;

import java.io.Serializable;

class Gene implements Serializable {
	private static final long serialVersionUID = -6319174282410756291L;

	final int geneInfo;
	private boolean mutable = true;

	Gene(int geneInfo) {
		this.geneInfo = geneInfo;
	}

	Gene mutate() {
		int geneInfo = this.geneInfo;
		if (mutable && GeneticsConstants.geneticsRandom.genDouble() <= GeneticsConstants.mutateChance) {
			geneInfo = geneInfo + (GeneticsConstants.geneticsRandom.genInt(2) == 1 ? 1 : -1);
		}
		return new Gene(geneInfo);
	}

	Gene unmutable() {
		this.mutable = false;
		return this;
	}
}