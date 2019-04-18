/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
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

package ns.components;

import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.utils.GU;
import ns.world.World;
import org.lwjgl.util.vector.Vector3f;

public class MovementComponent implements IComponent {
	static final int MOVE = 1;// 00000001
	static final int JUMP = 2;// 00000010
	private static final long serialVersionUID = 545727506141634743L;
	private static final float SPEED = 10f;
	private static final float JUMP_POWER = 1.2f;
	private static final float GRAVITY = 5f;

	private int config;
	private Vector3f vel;
	private Vector3f target = null;

	MovementComponent(int config) {
		if (config == 0) {
			throw new InstantiationError("Config cannot be 0, values accepted are 1, 2 or 3");
		}
		this.config = config;
		this.vel = new Vector3f();
	}

	public void setTarget(Vector3f target) {
		this.target = target;
	}

	public void update(Vector3f position, Entity e, Blueprint blueprint, World world, boolean ableToMove) {
		if ((config & MOVE) != 0 && ableToMove) {
			if (target != null) {
				Vector3f movement = Vector3f.sub(target, position, null);
				float rot = (float) Math.toDegrees(Vector3f.angle(movement, new Vector3f(0, 0, -1)));
				if (movement.x >= 0f)
					rot = 360f - rot;
				e.setRotY(rot);
				if (movement.lengthSquared() < 5f)
					target = null;
			} else {
				e.rotate(0, GU.random.genFloat() * 10f - 5f, 0);
			}
			float radyrot = (float) Math.toRadians(e.getRotY() + 180);
			vel.x = (float) (SPEED * DisplayManager.getInGameTimeSeconds() * Math.sin(radyrot));
			vel.z = (float) (SPEED * DisplayManager.getInGameTimeSeconds() * Math.cos(radyrot));
			if (blueprint.withinLimits(world.getTerrain().getHeight(position.x + vel.x, position.z + vel.z))) {
				position.x += vel.x;
				position.z += vel.z;
			} else if (target != null) {
				// TODO Make it go around the the zone witch is unreachable
			} else {
				e.rotate(0, GU.random.genFloat() * 20f - 10f, 0);
			}
		}
		if ((config & JUMP) != 0 && ableToMove) {
			float th = world.getTerrain().getHeight(position.x, position.z);
			if (position.y < th) {
				position.y = th;
				vel.y = JUMP_POWER;
			}
			position.y += vel.y;
			vel.y -= GRAVITY * DisplayManager.getInGameTimeSeconds();
		} else
			position.y = world.getTerrain().getHeight(position.x, position.z);
	}

	public int getConfig() {
		return config;
	}

	@Override
	public IComponent copy() {
		return new MovementComponent(config);
	}
}