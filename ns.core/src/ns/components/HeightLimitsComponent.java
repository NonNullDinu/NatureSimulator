package ns.components;

import org.lwjgl.util.vector.Vector2f;

public class HeightLimitsComponent implements IComponent {
	private Vector2f lim;

	public HeightLimitsComponent withLimit(Vector2f limit) {
		this.lim = limit;
		return this;
	}

	public boolean isWithinLimits(float y) {
		return lim.x < y && lim.y > y;
	}

	@Override
	public IComponent copy() {
		return this;
	}
}
