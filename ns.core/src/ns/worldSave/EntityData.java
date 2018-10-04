package ns.worldSave;

import ns.entities.Entity;
import org.lwjgl.util.vector.Vector3f;

public class EntityData extends Data {
	private static final long serialVersionUID = -2117931221477865196L;
	
	private BlueprintData blueprintData;
	private Vector3f position;
	private Vector3f rotation;

	public void setBlueprintData(BlueprintData blueprintData) {
		this.blueprintData = blueprintData;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	@Override
	public Entity asInstance() {
		Entity e = new Entity(blueprintData.asInstance(), position);
		e.rotate(rotation.x, rotation.y, rotation.z);
		return e;
	}
}