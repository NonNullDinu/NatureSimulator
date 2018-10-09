package ns.genetics;

import java.io.Serializable;

public class DNA implements Serializable {
	// GENE VALUES
	private static final int ALLOSOME_X = 0;
	private static final int ALLOSOME_Y = 1;
	// CHROMOSOME IDs
	private static final int ALLOSOME_1 = 44;
	private static final int ALLOSOME_2 = 45;
	private final Chromosome[] chromosomes;

	private DNA(Chromosome[] chromosomes) {
		this.chromosomes = chromosomes;
	}

	public static DNA _default(int id) {
		DNA dna = null;
		Chromosome[] chromosomes = new Chromosome[46];
		if (id == 1) {
			for (int i = 0; i < 46; i++) {
				if (i == ALLOSOME_1)
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
				if (i == ALLOSOME_1)
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
		System.out.println("Blending chromosomes");
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
		System.out.println("Finished blending chromosomes");
		return new DNA(chromosomes);
	}

	private DNA mutate() {
		Chromosome[] chromosomes = new Chromosome[this.chromosomes.length];
		System.out.println("Mutating chromosomes");
		for (int i = 0; i < chromosomes.length; i++)
			chromosomes[i] = (i < ALLOSOME_1 ? (this.chromosomes[i] != null ? this.chromosomes[i].mutate() : null) :
					(this.chromosomes[i] != null ? this.chromosomes[i].copy() : null)); //
		// Mutating the allosome chromosomes is a bad idea!!!
		System.out.println("Finish mutating chromosomes");
		return new DNA(chromosomes);
	}

	public DNA passedToOffspring() {
		return mutate();
	}

	public boolean isMale() {
		return chromosomes[ALLOSOME_2].genes[0].geneInfo == ALLOSOME_Y;
	}

	private Chromosome[] chromosomePair(int i) {
		return new Chromosome[]{
				chromosomes[i * 2], chromosomes[i * 2 + 1]
		};
	}

	public boolean isFemale() {
		return chromosomes[ALLOSOME_2].genes[0].geneInfo == ALLOSOME_X;
	}

	public int getAllosomeGeneData() {
		return chromosomes[ALLOSOME_2].genes[0].geneInfo;
	}
}