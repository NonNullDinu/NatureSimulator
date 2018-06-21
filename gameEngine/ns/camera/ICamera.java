package ns.camera;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Dinu B.
 * @since 1.0
 */
public abstract class ICamera implements CameraImplementation {
	public static ICamera createdCamera;

	protected Vector3f position;
	protected float rotX;
	protected float rotY;
	protected float rotZ;

	private Matrix4f projectionMatrix;

	public ICamera(Matrix4f projectionMatrix) {
		createdCamera = this;
		position = new Vector3f(0, 100, 300);
		this.projectionMatrix = projectionMatrix;
	}

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

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}