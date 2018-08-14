package ns.utils;

import ns.camera.ICamera;
import ns.entities.Entity;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public final class Maths {
	private Maths() {
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f position, float rotx, float roty, float rotz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(position);
		matrix.rotate((float) Math.toRadians(rotx), new Vector3f(1, 0, 0));
		matrix.rotate((float) Math.toRadians(roty), new Vector3f(0, 1, 0));
		matrix.rotate((float) Math.toRadians(rotz), new Vector3f(0, 0, 1));
		matrix.scale(new Vector3f(scale, scale, scale));
		return matrix;
	}
	
	public static Matrix4f createTreansformationMatrix(Entity e) {
		return createTransformationMatrix(e.getPosition(), e.getRotX(), e.getRotY(), e.getRotZ(), e.getScale());
	}
	
	public static Matrix4f createViewMatrix(ICamera camera) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix.rotate((float) Math.toRadians(camera.getRotX()), new Vector3f(1, 0, 0));
		matrix.rotate((float) Math.toRadians(camera.getRotY()), new Vector3f(0, 1, 0));
		matrix.rotate((float) Math.toRadians(camera.getRotZ()), new Vector3f(0, 0, 1));
		Vector3f camPos = camera.getPosition();
		Vector3f negCamPos = new Vector3f(-camPos.x, -camPos.y, -camPos.z);
		matrix.translate(negCamPos);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector2f center, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(center);
		matrix.scale(new Vector3f(scale.x, scale.y, 0.0f));
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f center, Vector2f scale, float rot) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(center);
		matrix.rotate((float) Math.toRadians(rot), new Vector3f(0, 0, 1));
		matrix.scale(new Vector3f(scale.x, scale.y, 0.0f));
		return matrix;
	}
}
