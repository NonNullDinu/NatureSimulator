package ns.rivers;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.display.DisplayManager;
import ns.terrain.Terrain;

public class WaterParticle {
	private Vector3f position;
	private float size;
	private Vector2f velocity;

	public WaterParticle(Vector3f position) {
		this.position = position;
		this.size = 4f;
		this.velocity = new Vector2f();
	}

	public boolean update(Terrain terrain) {
		Vector3f normal = terrain.getNormal(position);
		float spd = 5f * DisplayManager.getFrameTimeSeconds();
		velocity.scale(0.55f);
		Vector2f nrmvec = new Vector2f(normal.x, normal.z);
		nrmvec.scale(0.45f);
		Vector2f.add(velocity, nrmvec, velocity);
		position.x += velocity.x * spd;
		position.z += velocity.y * spd;
		position.y = terrain.getHeight(position.x, position.z);
		return (new Vector2f(normal.x, normal.z).length() < 0.3f && velocity.length() < 0.3f) || position.y < -size;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getSize() {
		return size;
	}
}