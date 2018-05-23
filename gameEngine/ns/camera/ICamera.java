package ns.camera;

import org.lwjgl.util.vector.Vector3f;

import ns.world.World;

/**
 * @author Dinu B.
 * @since 1.0
 */
public abstract class ICamera {
	public static ICamera createdCamera;

	protected Vector3f position;
	protected float rotX;
	protected float rotY;
	protected float rotZ;

	public ICamera() {
		createdCamera = this;
		position = new Vector3f(0, 100, 300);
	}

	public abstract void update(World world);

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void invertPitch() {
		rotX = -rotX;
	}
}