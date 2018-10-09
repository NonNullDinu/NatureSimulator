package ns.genetics;

import ns.utils.GU;

final class GeneticsConstants {
	static final double mutateChance = 0.000001;
	static final GU.Random geneticsRandom = new GU.Random();

	static {
		geneticsRandom.setSeed(geneticsRandom.genInt(1000000000));
	}

	private GeneticsConstants() {
	}
}
