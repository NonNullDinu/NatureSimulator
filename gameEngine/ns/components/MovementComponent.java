package ns.components;

import org.lwjgl.util.vector.Vector3f;

import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.utils.GU;
import ns.world.World;

public class MovementComponent implements IComponent {

	public static final int MOVE = 1;// 00000001
	public static final int JUMP = 2;// 00000010
	
	private static final float SPEED = 20f;
	private static final float JUMP_POWER = 1.2f;
	private static final float GRAVITY = 5f;

	private int config;
	private Vector3f vel;

	public MovementComponent(int config) {
		if(config == 0) {
			throw new InstantiationError("Config cannot be 0, values accepted are 1, 2 or 3");
		}
		this.config = config;
		this.vel = new Vector3f();
	}

	public void update(Vector3f position, Entity e, Blueprint blueprint, World world) {
		if((config & MOVE) != 0) {
			e.rotate(0, GU.random.genFloat() * 10f - 5f, 0);
			
			float radyrot = (float) Math.toRadians(e.getRotY() + 180);
			vel.x = (float) (SPEED * DisplayManager.getFrameTimeSeconds() * Math.sin(radyrot));
			vel.z = (float) (SPEED * DisplayManager.getFrameTimeSeconds() * Math.cos(radyrot));
			e.increasePosition(vel.x, 0, vel.z);
		}
		if((config & JUMP) != 0) {
			float th = world.getTerrain().getHeight(position.x, position.z);
			if(position.y < th) {
				position.y = th;
				vel.y = JUMP_POWER;
			}
			position.y += vel.y;
			vel.y -= GRAVITY * DisplayManager.getFrameTimeSeconds();
		}
	}

	public int getConfig() {
		return config;
	}
}