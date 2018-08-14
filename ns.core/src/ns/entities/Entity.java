package ns.entities;

import ns.components.BiomeSpreadComponent;
import ns.components.Blueprint;
import ns.components.CustomColorsComponent;
import ns.openglObjects.VAO;
import ns.world.World;
import ns.worldSave.EntityData;
import ns.worldSave.SerializableWorldObject;
import org.lwjgl.util.vector.Vector3f;

public class Entity implements SerializableWorldObject {

	private Blueprint blueprint;
	
	private Vector3f position;
	private float rotX;
	private float rotY;
	private float rotZ;
	private float scale;

	public Entity(Blueprint blueprint, Vector3f position) {
		this.blueprint = blueprint;
		this.position = position;
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
		this.scale = 1;
	}

	public VAO getModel() {
		return blueprint.getModel().getModel();
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

	public float getScale() {
		return scale;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void rotate(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public BiomeSpreadComponent getBiomeSpreadComponent() {
		return blueprint.getBiomeSpread();
	}
	
	public void update(World w) {
		blueprint.move(this, w);
	}

	public CustomColorsComponent getCustomColors() {
		return blueprint.getCustomColors();
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	@Override
	public EntityData asData() {
		EntityData data = new EntityData();
		data.setPosition(position);
		data.setBlueprintData(blueprint.asData());
		return data;
	}
}
