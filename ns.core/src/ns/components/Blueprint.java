package ns.components;

import ns.entities.Entity;
import ns.world.World;
import ns.worldSave.BlueprintData;
import ns.worldSave.SerializableWorldObject;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		data.setBlueprint(this);
		return data;
	}

	public String getFolder() {
		return objectName;
	}

	public Blueprint withDefaultCustomColors() {
		List<Vector3f> cc = new ArrayList<>();
		if (objectName.equals("1000") || objectName.equals("1001") || objectName.equals("1002")
				|| objectName.equals("1006") || objectName.equals("1010")) {
			cc.add(new Vector3f(0f, 0.6f, 0f));
		} else if (objectName.equals("1004") || objectName.equals("1005")) {
			cc.add(new Vector3f(0.002494f, 0.350834f, 0.000000f));
			cc.add(new Vector3f(0.000000f, 0.307499f, 0.002174f));
		}
		return this.withCuctomColors(new CustomColorsComponent(cc));
	}

	public int flags(int i) {
		switch (i) {
		case 0: // Model
			int front = Integer.parseInt(objectName) - 999;
			return front;
		case 1: // Movement and biome spread 1
			MovementComponent mvm = getMovement();
			int front2 = 0;
			if (mvm != null)
				front2 = mvm.getConfig();
			BiomeSpreadComponent comp = getBiomeSpread();
			int back = 0;
			if (comp != null)
				back = comp.getBiome().getId();
			return (front2 << 4) | back;
		case 2: // Biome spread part 2
			BiomeSpreadComponent comp2 = getBiomeSpread();
			if (comp2 != null) {
				return (int) comp2.getMinMax().x;
			}
			return 0;
		case 3: // Biome spread part 3
			BiomeSpreadComponent comp3 = getBiomeSpread();
			if (comp3 != null) {
				return (int) comp3.getMinMax().y;
			}
			return 0;
		default:
			return 0;
		}
	}
}