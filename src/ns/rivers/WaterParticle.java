package ns.rivers;

import java.io.IOException;
import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.display.DisplayManager;
import ns.terrain.Terrain;

public class WaterParticle implements Serializable {
	private static final long serialVersionUID = -5191206774216962023L;

	private Vector3f position;
	private float size;
	private Vector2f velocity;
	private float deltaY;
	private boolean reachedBaseLake;

	private Vector3f[] prevPos = new Vector3f[River.COUNT];
	protected final int idx;

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if(prevPos == null) {
			prevPos = new Vector3f[River.COUNT * 2];
			for(int i = 0; i < prevPos.length; i++)
				prevPos[i] = new Vector3f(position);
		}
	}

	public WaterParticle(Vector3f position, int idx) {
		this.position = position;
		this.size = 0f;
		this.velocity = new Vector2f();
		for(int i = 0; i < prevPos.length; i++)
			prevPos[i] = new Vector3f(position);
		this.idx = idx;
	}

	public boolean update(Terrain terrain, float sourceHeight) {
		Vector3f normal = terrain.getNormal(position);
		float spd = 5f * DisplayManager.getFrameTimeSeconds();
		velocity.scale(0.7f);
		Vector2f nrmvec = new Vector2f(normal.x, normal.z);
		nrmvec.scale(0.3f);
		Vector2f.add(velocity, nrmvec, velocity);
		
		for(int i = prevPos.length - 1; i > 0; i--)
			prevPos[i] = prevPos[i - 1];
		prevPos[0] = new Vector3f(position);
		position.x += velocity.x * spd;
		position.z += velocity.y * spd;
		deltaY = -position.y;
		position.y = terrain.getHeight(position.x, position.z);
		deltaY += position.y;
		size = (sourceHeight - position.y) / sourceHeight * 3f;
		return velocity.length() < 0.1f || (reachedBaseLake = position.y <= -8f);
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
		deltaY = terrain.getHeight(position.x + velocity.x, position.z + velocity.y) - position.y;
	}

	public boolean reachedBaseLake() {
		return reachedBaseLake;
	}

	public void setPosition(Vector3f position, float srcY, Vector2f vel, float deltaY) {
//		velocity.x = vel.x;
//		velocity.y = vel.y;
//		this.deltaY = deltaY;
		velocity.x = position.x - this.position.x;
		velocity.y = position.z - this.position.z;
		deltaY = position.y - this.position.y;
		for(int i = prevPos.length - 1; i > 0; i--)
			prevPos[i] = prevPos[i - 1];
		prevPos[0] = new Vector3f(position);
		this.position = position;
//		if (position.x == 0 && position.y == 0 && position.z == 0)
//			System.out.println("0 Found");
		size = (srcY - position.y) / srcY * 3f;
	}

	public Vector3f getPrevPosition() {
		return prevPos[prevPos.length - 1];
	}
}