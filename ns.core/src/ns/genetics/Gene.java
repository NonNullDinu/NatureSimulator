package ns.genetics;

import java.io.Serializable;

class Gene implements Serializable {
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