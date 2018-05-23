package ns.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformVec2 extends UniformVar {

	public UniformVec2(int location) {
		super(location);
	}
	
	public void load(Vector2f value) {
		GL20.glUniform2f(location, value.x, value.y);
	}
}
