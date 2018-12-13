package ns.entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Moon extends Light {
	public Moon(Vector3f direction, Vector3f color, Vector2f bias) {
		super(direction, color, bias);
	}

	public void update(Sun sun) {
		dir.x = -sun.dir.x;
		dir.y = -sun.dir.y;
		dir.z = -sun.dir.z;
	}
}
