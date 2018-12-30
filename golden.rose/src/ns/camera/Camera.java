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

import ns.display.DisplayManager;
import ns.utils.GU;
import ns.utils.Maths;
import ns.world.World;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Camera extends ICamera {
	private static final float DIST_AB_TER = 10f;
	private static final float SENSITIVITY = 0.1f;

	private final Vector3f point;
	private final Vector3f onTerrainPoint;
	private float distance;
	private float angle = 180;

	private int flags = 0;

	public Camera(Matrix4f projectionMatrix) {
		super(projectionMatrix);
		point = new Vector3f();
		onTerrainPoint = new Vector3f();
		distance = position.length();
		rotX = (float) Math
				.toDegrees(Vector3f.angle(new Vector3f(-position.x, -position.y, -position.z), new Vector3f(0, 0, -1)));
	}

	@Override
	public void update(World world) {
		Vector3f mouseDelta = new Vector3f(GU.mouseDelta);
		if (GU.mouseButtons.y) {
			if (mouseDelta.lengthSquared() != 0f) {
				mouseDelta.scale(SENSITIVITY);
				Vector3f delta = new Vector3f();
				float coef = 120f * DisplayManager.getDelta();
				delta.x = coef * -mouseDelta.x;
				delta.z = coef * mouseDelta.y;
				Matrix4f toWorldRotation = Maths.createTransformationMatrix(new Vector3f(), 0, -rotY, 0, 1);
				Vector4f result = Matrix4f.transform(toWorldRotation, new Vector4f(delta.x, 0, delta.z, 0), null);
				position.x += result.x;
				position.z += result.z;
				point.x += result.x;
				point.z += result.z;
				onTerrainPoint.x = point.x;
				onTerrainPoint.z = point.z;
				onTerrainPoint.y = world.getTerrain().getHeight(onTerrainPoint.x, onTerrainPoint.z);
			}
		}
		if (GU.mouseButtons.x) {
			if (new Vector2f(mouseDelta.x, mouseDelta.y).lengthSquared() != 0f) {
				angle -= mouseDelta.x;
				rotX -= mouseDelta.y;
				if (rotX < -90f)
					rotX = -90f;
				if (rotX > 90f)
					rotX = 90f;
				if (angle < 0f)
					angle += 360f;
				if (angle >= 360f)
					angle -= 360f;
			}
			float dw = mouseDelta.z;
			if (dw != 0) {
				distance -= dw * 0.1f;
			}
		}
		float toTerrainDistance = Vector3f.sub(onTerrainPoint, position, null).length();
		Vector3f onPointPt = new Vector3f(point.x, GU.clamp(position.y, 0f, onTerrainPoint.y), point.z);
		float dist = Vector3f.sub(onPointPt, position, null).length();

		// Recalculate position
		if (dist < 200f) {
			point.y = onTerrainPoint.y;
			if ((flags & 1) == 0) {
				flags = 3;
			} else
				flags = 1;
		} else {
			point.y = 0f;
			if ((flags & 1) != 0) {
				flags = 2;
			} else
				flags = 0;
		}
		if ((flags & 2) == 2) {
			if ((flags & 1) != 0) {
				distance = toTerrainDistance;
				rotX -= Math.toDegrees(Vector3f.angle(Vector3f.sub(point, position, null),
						Vector3f.sub(onTerrainPoint, position, null)));
			} else {
				distance = Vector3f.sub(point, position, null).length();
				rotX += Math.toDegrees(Vector3f.angle(Vector3f.sub(point, position, null),
						Vector3f.sub(onTerrainPoint, position, null)));
			}
		}
		position.y = point.y + (float) (distance * Math.sin(Math.toRadians(rotX)));
		position.y = Math.max(position.y, DIST_AB_TER);
		float xz = (float) (distance * Math.cos(Math.toRadians(rotX)));
		float dx = (float) (xz * Math.sin(Math.toRadians(angle)));
		float dz = (float) (xz * Math.cos(Math.toRadians(angle)));
		position.x = point.x - dx;
		position.z = point.z - dz;
		rotY = 180 - angle;
		while (position.y < world.getTerrain().getHeight(position.x, position.z) + DIST_AB_TER) {
			if (rotX < 90f)
				rotX += 0.1f;
			else
				distance += 2f;
			position.y = point.y + (float) (distance * Math.sin(Math.toRadians(rotX)));
			xz = (float) (distance * Math.cos(Math.toRadians(rotX)));
			dx = (float) (xz * Math.sin(Math.toRadians(angle)));
			dz = (float) (xz * Math.cos(Math.toRadians(angle)));
			position.x = point.x - dx;
			position.z = point.z - dz;
			rotY = 180 - angle;
		}
		super.viewMatrix = Maths.createViewMatrix(this);
	}
}