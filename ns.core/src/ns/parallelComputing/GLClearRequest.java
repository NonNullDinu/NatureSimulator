package ns.parallelComputing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class GLClearRequest extends Request {

	private final int mask;
	private final Vector3f color;

	public GLClearRequest(int mask, Vector3f color) {
		this.mask = mask;
		this.color = color;
	}

	@Override
	public void execute() {
		GL11.glClearColor(color.x, color.y, color.z, 1);
		GL11.glClear(mask);
	}
}