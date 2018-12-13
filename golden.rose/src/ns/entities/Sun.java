package ns.entities;

import ns.time.DayNightCycle;
import ns.utils.GU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Sun extends Light {
	public Sun(Vector3f direction, Vector3f color, Vector2f bias) {
		super(direction, color, bias);
	}

	public void update() {
		double angle = Math.toRadians(GU.time.t / DayNightCycle.H_S_DURATION % 24f / 24f * 360f + 90f /*??*/);
		dir.x = (float) Math.cos(angle);
		dir.y = (float) Math.sin(angle);
	}
}
