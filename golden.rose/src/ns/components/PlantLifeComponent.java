package ns.components;

import org.lwjgl.util.vector.Vector2f;

public class PlantLifeComponent extends LifeComponent {
	private static final long serialVersionUID = 8859820823277404834L;

	private Vector2f lim;

	PlantLifeComponent(float totalLifespan) {
		super(totalLifespan);
	}

	public PlantLifeComponent withLimits(Vector2f limits) {
		this.lim = limits;
		return this;
	}

	@Override
	public boolean isWithinHeightLimits(float y) {
		return y >= lim.x && y <= lim.y;
	}

	public Vector2f getLimits() {
		return lim;
	}
}