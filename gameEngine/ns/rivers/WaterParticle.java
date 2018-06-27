package ns.rivers;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.display.DisplayManager;
import ns.terrain.Terrain;

public class WaterParticle {
	private Vector3f position;
	private float size;
	private Vector2f velocity;
	private float deltaY;

	public WaterParticle(Vector3f position) {
		this.position = position;
		this.size = 0f;
		this.velocity = new Vector2f();
	}

	public boolean update(Terrain terrain, float sourceHeight) {
		Vector3f normal = terrain.getNormal(position);
		float spd = 5f * DisplayManager.getFrameTimeSeconds();
		velocity.scale(0.55f);
		Vector2f nrmvec = new Vector2f(normal.x, normal.z);
		nrmvec.scale(0.45f);
		Vector2f.add(velocity, nrmvec, velocity);
		position.x += velocity.x * spd;
		position.z += velocity.y * spd;
		deltaY = -position.y;
		position.y = terrain.getHeight(position.x, position.z);
		deltaY += position.y;
		size = (sourceHeight - position.y) / sourceHeight * 3f;
		return (new Vector2f(normal.x, normal.z).length() < 0.3f && velocity.length() < 0.3f) || position.y <= -5f;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public float getSize() {
		return size;
	}

	public float deltaY() {
		return deltaY;
	}

	public void updateVel(Terrain terrain) {
		Vector3f normal = terrain.getNormal(position);
		velocity.scale(0.55f);
		Vector2f nrmvec = new Vector2f(normal.x, normal.z);
		nrmvec.scale(0.45f);
		Vector2f.add(velocity, nrmvec, velocity);
	}
}