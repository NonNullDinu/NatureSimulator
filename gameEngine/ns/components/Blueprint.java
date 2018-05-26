package ns.components;

import java.util.HashMap;
import java.util.Map;

import ns.entities.Entity;
import ns.world.World;
import ns.worldSave.BlueprintData;
import ns.worldSave.SerializableWorldObject;

public class Blueprint implements SerializableWorldObject {

	protected static final int MODEL = 0;
	protected static final int BIOME_SPREAD = 1;
	protected static final int MOVEMENT = 2;
	protected static final int CUSTOM_COLORS = 3;

	private Map<Integer, IComponent> components = new HashMap<>();
	private String objectName;

	protected Blueprint(String objectName) {
		this.objectName = objectName;
	}

	protected Blueprint withComponent(int id, IComponent c) {
		components.put(id, c);
		return this;
	}

	protected Blueprint withBiomeSpread(BiomeSpreadComponent c) {
		return withComponent(BIOME_SPREAD, c);
	}

	public BiomeSpreadComponent getBiomeSpread() {
		return (BiomeSpreadComponent) components.get(BIOME_SPREAD);
	}

	protected Blueprint withModel(ModelComponent c) {
		return withComponent(MODEL, c);
	}

	public ModelComponent getModel() {
		return (ModelComponent) components.get(MODEL);
	}

	protected Blueprint withMovement(MovementComponent c) {
		return withComponent(MOVEMENT, c);
	}

	public MovementComponent getMovement() {
		return (MovementComponent) components.get(MOVEMENT);
	}

	public void move(Entity e, World w) {
		MovementComponent moveC = getMovement();
		if (moveC != null) {
			moveC.update(e.getPosition(), e, this, w);
		}
	}

	protected Blueprint withCuctomColors(CustomColorsComponent c) {
		return withComponent(CUSTOM_COLORS, c);
	}

	public CustomColorsComponent getCustomColors() {
		return (CustomColorsComponent) components.get(CUSTOM_COLORS);
	}

	@Override
	public BlueprintData asData() {
		BlueprintData data = new BlueprintData();
		data.setObjectName(objectName);
		return data;
	}

	public String getFolder() {
		return objectName;
	}
}