package ns.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

public class UniformVec4 extends UniformVar {

	public UniformVec4(String name) {
		super(name);
	}
	
	public void load(Vector4f value) {
		GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
	}
}
