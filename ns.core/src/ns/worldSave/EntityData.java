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