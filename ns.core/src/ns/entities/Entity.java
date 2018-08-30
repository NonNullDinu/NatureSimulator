package ns.entities;

import ns.components.BiomeSpreadComponent;
import ns.components.Blueprint;
import ns.components.CustomColorsComponent;
import ns.components.LifeComponent;
import ns.display.DisplayManager;
import ns.openglObjects.VAO;
import ns.utils.GU;
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
	private Vector3f DEAD_ROT_AXIS;
	private float alpha;

	public Entity(Blueprint blueprint, Vector3f position) {
		this.blueprint = blueprint;
		this.position = position;
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
		this.scale = 1f;
		this.alpha = 1f;
		double ang = Math.toRadians(GU.random.genFloat() * 360f);
		this.DEAD_ROT_AXIS = new Vector3f((float) Math.sin(ang), 0, (float) Math.cos(ang));
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

	public void rotate(Vector3f axis, float ang) {
		this.rotate(ang * axis.x, ang * axis.y, ang * axis.z);
	}

	public BiomeSpreadComponent getBiomeSpreadComponent() {
		return blueprint.getBiomeSpread();
	}

	public void update(World w) {
		LifeComponent lc = getLifeComponent();
		if (lc != null) {
			if (lc.isDead()) {
				rotate(DEAD_ROT_AXIS, 30f * DisplayManager.getFrameTimeSeconds());
				if (Math.abs(rotX) >= 90f || Math.abs(rotZ) >= 90f) {
					alpha -= 0.2f * DisplayManager.getFrameTimeSeconds();
					if (alpha <= 0f) {
						w.remove(this);
						// TODO make folder 1013 with meat model and create blueprint for it. Uncomment when done
//						if (lc instanceof AnimalLifeComponent) {
//							w.add(new Entity(BlueprintCreator.createBlueprintFor("1013"), new Vector3f(position.x, w.getTerrain().getHeight(position.x, position.z), position.z)));
//						}
					}
				}
			} else {
				lc.update();
			}
		}
		blueprint.move(this, w);
	}

	public LifeComponent getLifeComponent() {
		return blueprint.getLifeComponent();
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

	public float getFoodAmount() {
		return blueprint.getFoodComponent().amount;
	}

	public float getAlpha() {
		return alpha;
	}
}
