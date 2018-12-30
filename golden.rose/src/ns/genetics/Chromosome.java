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
import java.util.Arrays;

public class Chromosome implements Serializable {
	private static final long serialVersionUID = 1399736957865751125L;

	final Gene[] genes;

	Chromosome(Gene[] genes) {
		this.genes = genes;
	}

	Chromosome mutate() {
		Gene[] genes = new Gene[this.genes.length];
		for (int i = 0; i < genes.length; i++) {
			genes[i] = this.genes[i].mutate();
		}
		return new Chromosome(genes);
	}

	public Chromosome copy() {
		return new Chromosome(Arrays.copyOf(genes, genes.length));
	}
}