package ns.worldSave;

import org.lwjgl.util.vector.Vector3f;

import ns.entities.Entity;

public class EntityData extends Data {
	private static final long serialVersionUID = -2117931221477865196L;
	
	private BlueprintData blueprintData;
	private Vector3f position;

	public void setBlueprintData(BlueprintData blueprintData) {
		this.blueprintData = blueprintData;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	@Override
	public Entity asInstance() {
		return new Entity(blueprintData.asInstance(), position);
	}
}