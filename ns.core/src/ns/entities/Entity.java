package ns.entities;

import ns.components.*;
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
		boolean ableToMove = true;
		if (lc != null) {
			if (lc.isDead()) {
				ableToMove = false;
				rotate(DEAD_ROT_AXIS, 30f * DisplayManager.getFrameTimeSeconds());
				alpha -= 0.4f * DisplayManager.getFrameTimeSeconds();
				if ((Math.abs(rotX) >= 75f || Math.abs(rotZ) >= 75f) && alpha <= 0f) {
					w.remove(this);
					if (lc instanceof AnimalLifeComponent) {
						Entity meatLeftBehind = new Entity(BlueprintCreator.createBlueprintFor("1013"),
								new Vector3f(position.x,
										w.getTerrain().getHeight(position.x, position.z), position.z));
						meatLeftBehind.rotate(0f, 360f * GU.random.genFloat(), 0f);
						w.add(meatLeftBehind);
					}
				}
			} else {
				lc.update();
			}
		}
		blueprint.move(this, w, ableToMove);
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
		data.setRotation(new Vector3f(rotX, rotY, rotZ));
		return data;
	}

	public FoodComponent getFoodComp() {
		return blueprint.getFoodComponent();
	}

	public float getAlpha() {
		return alpha;
	}

	public boolean isHeightWithinLimits(float y) {
		HeightLimitsComponent limitsComponent = blueprint.getHeightLimits();
		return limitsComponent == null || limitsComponent.isWithinLimits(y);
	}
}
