/*
 * Copyright (C) 2018  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
	private final Matrix4f projectionMatrix;
	protected float rotZ;
	float rotX;
	float rotY;
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