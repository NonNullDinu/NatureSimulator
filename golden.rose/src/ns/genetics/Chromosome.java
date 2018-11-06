package ns.genetics;

import java.io.Serializable;
import java.util.Arrays;

public class Chromosome implements Serializable {
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