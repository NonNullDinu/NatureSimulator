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

import org.lwjgl.util.vector.Vector2f;

import java.io.Serializable;

public class DNA implements Serializable {
	private static final long serialVersionUID = 7075152065422254608L;

	// GENE VALUES
	private static final int ALLOSOME_X = 0;
	private static final int ALLOSOME_Y = 1;

	// CHROMOSOME IDs
	private static final int ALLOSOME_1 = 44;
	private static final int ALLOSOME_2 = 45;

	private static final int HEIGHT_LIMITS = 0;

	private final Chromosome[] chromosomes;

	private DNA(Chromosome[] chromosomes) {
		this.chromosomes = chromosomes;
	}

	public static DNA _default(int id) {
		DNA dna = null;
		Chromosome[] chromosomes = new Chromosome[46];
		if (id == 1) {
			for (int i = 0; i < 46; i++) {
				if (i == HEIGHT_LIMITS)
					chromosomes[i] = new Chromosome(new Gene[]{
							new Gene(20),
							new Gene(80),
							new Gene(0).unmutable(),
							new Gene(100).unmutable()
					});
				else if (i == ALLOSOME_1)
					chromosomes[i] =
							new Chromosome(new Gene[]{new Gene(ALLOSOME_X)});
				else if (i == ALLOSOME_2)
					chromosomes[i] =
							new Chromosome(new Gene[]{new Gene(GeneticsConstants.geneticsRandom.genInt(2) + ALLOSOME_X)});
			}
			dna = new DNA(chromosomes);
		}
		if (id == 2) {
			for (int i = 0; i < 46; i++) {
				if (i == HEIGHT_LIMITS)
					chromosomes[i] = new Chromosome(new Gene[]{
							new Gene(20),
							new Gene(80),
							new Gene(0).unmutable(),
							new Gene(100).unmutable()
					});
				else if (i == ALLOSOME_1)
					chromosomes[i] =
							new Chromosome(new Gene[]{new Gene(ALLOSOME_X)});
				else if (i == ALLOSOME_2)
					chromosomes[i] =
							new Chromosome(new Gene[]{new Gene(GeneticsConstants.geneticsRandom.genInt(2) + ALLOSOME_X)});
			}
			dna = new DNA(chromosomes);
		}
		return dna;
	}

	public static DNA blend(DNA a, DNA b) {
		Chromosome[] chromosomes = new Chromosome[a.chromosomes.length];
		for (int i = 0; i < chromosomes.length; i++) {
			if (a.chromosomes[i] == null && b.chromosomes[i] == null) {
				chromosomes[i] = null;
			} else if (a.chromosomes[i] == null)
				chromosomes[i] = b.chromosomes[i];
			else if (b.chromosomes[i] == null)
				chromosomes[i] = a.chromosomes[i];
			else {
				double d = GeneticsConstants.geneticsRandom.genDouble();
				if (d <= 50.0) {
					chromosomes[i] = a.chromosomes[i].copy();
				} else chromosomes[i] = b.chromosomes[i].copy();
			}
		}
		return new DNA(chromosomes);
	}

	private DNA mutate() {
		Chromosome[] chromosomes = new Chromosome[this.chromosomes.length];
		for (int i = 0; i < chromosomes.length; i++)
			chromosomes[i] = (i < ALLOSOME_1 ? (this.chromosomes[i] != null ? this.chromosomes[i].mutate() : null) :
					(this.chromosomes[i] != null ? this.chromosomes[i].copy() : null)); //
		// Mutating the allosome chromosomes is a bad idea!!!
		return new DNA(chromosomes);
	}

	public DNA passedToOffspring() {
		return mutate();
	}

	public boolean isMale() {
		return chromosomes[ALLOSOME_2].genes[0].geneInfo == ALLOSOME_Y;
	}

	public boolean isFemale() {
		return chromosomes[ALLOSOME_2].genes[0].geneInfo == ALLOSOME_X;
	}

	public int getAllosomeGeneData() {
		return chromosomes[ALLOSOME_2].genes[0].geneInfo;
	}

	public Vector2f getHeightLimits() {
		return new Vector2f(chromosomes[HEIGHT_LIMITS].genes[0].geneInfo, chromosomes[HEIGHT_LIMITS].genes[1].geneInfo);
	}

	public Vector2f deathLimits() {
		return new Vector2f(chromosomes[HEIGHT_LIMITS].genes[2].geneInfo, chromosomes[HEIGHT_LIMITS].genes[3].geneInfo);
	}
}