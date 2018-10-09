package ns.camera;

import ns.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Dinu B.
 * @since 1.0
 */
public abstract class ICamera implements CameraImplementation {
	public static ICamera createdCamera;

	final Vector3f position;
	float rotX;
	float rotY;
	protected float rotZ;

	private final Matrix4f projectionMatrix;
	Matrix4f viewMatrix;

	public ICamera(Matrix4f projectionMatrix) {
		createdCamera = this;
		position = new Vector3f(0, 100, 300);
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(this);
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
		this.viewMatrix = Maths.createViewMatrix(this);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public void addToPositionNoViewMatUpdate(float dx, float dy, float dz) {
		position.x += dx;
		position.y += dy;
		position.z += dz;
	}
}