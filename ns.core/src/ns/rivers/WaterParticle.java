package ns.rivers;

import ns.display.DisplayManager;
import ns.terrain.Terrain;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class WaterParticle implements Serializable {
	private static final long serialVersionUID = -5191206774216962023L;
	private WaterParticle previous;

	private Vector3f position;
	private float size;
	private final Vector2f velocity;
	private float deltaY;
	private boolean reachedBaseLake;

	private Vector3f[] prevPos = new Vector3f[River.COUNT];
	final int idx;
	private List<WaterParticle> follows;

	WaterParticle(Vector3f position, int idx) {
		this.position = position;
		this.size = 0f;
		this.velocity = new Vector2f();
		for (int i = 0; i < prevPos.length; i++)
			prevPos[i] = new Vector3f(position);
		this.idx = idx;
	}

	WaterParticle(Vector3f position, int idx, WaterParticle previous) {
		this.position = position;
		this.size = 0f;
		this.velocity = new Vector2f();
		for (int i = 0; i < prevPos.length; i++)
			prevPos[i] = new Vector3f(position);
		this.idx = idx;
		this.previous = previous;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (prevPos == null) {
			prevPos = new Vector3f[River.COUNT];
			for (int i = 0; i < prevPos.length; i++)
				prevPos[i] = new Vector3f(position);
		}
	}

	public boolean update(Terrain terrain, float sourceHeight) {
		Vector3f normal = terrain.getNormal(position);
		float spd = 5f * DisplayManager.getFrameTimeSeconds();
		velocity.scale(0.7f);
		Vector2f nrmvec = new Vector2f(normal.x, normal.z);
		nrmvec.scale(0.3f);
		Vector2f.add(velocity, nrmvec, velocity);

		for (int i = prevPos.length - 1; i > 0; i--)
			prevPos[i] = prevPos[i - 1];
		prevPos[0] = new Vector3f(position);
		position.x += velocity.x * spd;
		position.z += velocity.y * spd;
		deltaY = -position.y;
		position.y = terrain.getHeight(position.x, position.z);
		deltaY += position.y;
		size = (sourceHeight - position.y) / sourceHeight * 3f;
		return velocity.length() < 0.1f || (reachedBaseLake = position.y <= -4f);
	}

	public Vector3f getPosition() {
		return position;
	}

	Vector2f getVelocity() {
		return velocity;
	}

	public float getSize() {
		return size;
	}

	float deltaY() {
		return deltaY;
	}

	void updateVel(Terrain terrain) {
		Vector3f normal = terrain.getNormal(position);
		velocity.scale(0.55f);
		Vector2f nrmvec = new Vector2f(normal.x, normal.z);
		nrmvec.scale(0.45f);
		Vector2f.add(velocity, nrmvec, velocity);
		deltaY = terrain.getHeight(position.x + velocity.x, position.z + velocity.y) - position.y;
	}

	boolean reachedBaseLake() {
		return reachedBaseLake;
	}

	void setPosition(Vector3f position, float srcY) {
		velocity.x = position.x - this.position.x;
		velocity.y = position.z - this.position.z;
		this.deltaY = position.y - this.position.y;
		for (int i = prevPos.length - 1; i > 0; i--)
			prevPos[i] = prevPos[i - 1];
		prevPos[0] = new Vector3f(position);
		this.position = position;
		size = (srcY - position.y) / srcY * 3f;
	}


	public void setPosition(Vector3f position, float srcY, Vector2f vel, float deltaY) {
		velocity.x = vel.x;
		velocity.y = vel.y;
		this.deltaY = deltaY;
		for (int i = prevPos.length - 1; i > 0; i--)
			prevPos[i] = prevPos[i - 1];
		prevPos[0] = new Vector3f(position);
		this.position = position;
		size = (srcY - position.y) / srcY * 3f;
	}

	Vector3f getPrevPosition() {
		return prevPos[prevPos.length - 1];
	}

	public WaterParticle getPrev() {
		return previous;
	}

	public void setPrevious(WaterParticle p) {
		this.previous = p;
	}

	public void addFollows(WaterParticle particle) {
		if (follows == null)
			follows = new ArrayList<>();
		follows.add(particle);
	}

	public void removeFromFollowers() {
		if (follows != null)
			for (WaterParticle particle : follows)
				particle.setPrevious(null);
	}
}