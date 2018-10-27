package ns.entities;

import ns.components.*;
import ns.display.DisplayManager;
import ns.genetics.DNA;
import ns.openglObjects.VAO;
import ns.utils.GU;
import ns.world.World;
import ns.worldSave.EntityData;
import ns.worldSave.SerializableWorldObject;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Entity implements SerializableWorldObject {

	private final Blueprint blueprint;

	private final Vector3f position;
	private float rotX;
	private float rotY;
	private float rotZ;
	private final float scale;
	private final Vector3f DEAD_ROT_AXIS;
	private float alpha;
	private Entity partner;

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

	private void rotate(Vector3f axis, float ang) {
		this.rotate(ang * axis.x, ang * axis.y, ang * axis.z);
	}

	public BiomeSpreadComponent getBiomeSpreadComponent() {
		return blueprint.getBiomeSpread();
	}

	public void update(World w) {
		LifeComponent lc = getLifeComponent();
		boolean ableToMove = true;
		Vector2f deathLim = null;
		if (lc != null) {
			if (lc.isDead()) {
				ableToMove = false;
				rotate(DEAD_ROT_AXIS, 30f * DisplayManager.getInGameTimeSeconds());
				alpha -= 0.4f * DisplayManager.getInGameTimeSeconds();
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
				if (lc instanceof AnimalLifeComponent) {
					AnimalLifeComponent alc = ((AnimalLifeComponent) lc);
					deathLim = alc.getDna().deathLimits();
					if (alc.isOffspringCreating()) {
						if (partner == null) {
							partner = w.closestEntity(position,
									(Entity e) -> !e.equals(this) && e.partner != null && e.blueprint.getFolder().equals(blueprint.getFolder()) && alc.getDna()
											.getAllosomeGeneData() != ((AnimalLifeComponent) e.getLifeComponent()).getDna()
											.getAllosomeGeneData());
							partner.setPartner(this);
						}
						if (Vector3f.sub(partner.getPosition(), position, null).lengthSquared() <= 25f) {
							alc.setOffspring(false);
							Entity e = new Entity(this.blueprint.copy(), new Vector3f(position));

							AnimalLifeComponent alc2 = ((AnimalLifeComponent) partner.getLifeComponent());
							((AnimalLifeComponent) e.getLifeComponent()).withDNA(DNA.blend(alc.getDna().passedToOffspring(),
									alc2.getDna().passedToOffspring()));

							w.add(e);
						} else {
							blueprint.setMovementTarget(partner.position);
							partner.blueprint.setMovementTarget(position);
						}
					}
				}
			}
		}
		if (ableToMove) {
			ableToMove = !GU.time.isNight();
		}
		blueprint.move(this, w, ableToMove);
		if (deathLim != null) {
			if (position.y < deathLim.x || position.y > deathLim.y)
				lc.setDead(true);
		}
	}

	private void setPartner(Entity partner) {
		this.partner = partner;
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
		data.setBlueprint(blueprint);
		data.setRotation(new Vector3f(rotX, rotY, rotZ));
		return data;
	}

	public FoodComponent getFoodComp() {
		return blueprint.getFoodComponent();
	}

	public float getAlpha() {
		return alpha;
	}

	public ModelComponent getModelComponent() {
		return blueprint.getModel();
	}
}
