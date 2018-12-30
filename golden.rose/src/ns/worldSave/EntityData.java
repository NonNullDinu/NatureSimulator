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

package ns.worldSave;

import ns.components.Blueprint;
import ns.components.BlueprintInputStream;
import ns.entities.Entity;
import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;

public class EntityData extends Data {
	private static final long serialVersionUID = -2117931221477865196L;

	private transient Blueprint blueprint;
	private Vector3f position;
	private Vector3f rotation;

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		blueprint.writeTo(out);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.blueprint = new BlueprintInputStream(in).read_new();
	}

	@Override
	public Entity asInstance() {
		Entity e = new Entity(blueprint, position);
		e.rotate(rotation.x, rotation.y, rotation.z);
		return e;
	}

	public void setBlueprint(Blueprint blueprint) {
		this.blueprint = blueprint;
	}
}