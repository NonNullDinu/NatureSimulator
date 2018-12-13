package ns.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

public class UniformVec3 extends UniformVar {

	public UniformVec3(String name) {
		super(name);
	}

	public void load(Vector3f value) {
		GL20.glUniform3f(location, value.x, value.y, value.z);
	}
}
