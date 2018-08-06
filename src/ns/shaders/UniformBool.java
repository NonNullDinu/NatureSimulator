package ns.shaders;

import org.lwjgl.opengl.GL20;

public class UniformBool extends UniformVar {

	public UniformBool(String name) {
		super(name);
	}
	
	public void load(boolean value) {
		GL20.glUniform1i(location, (value ? 1 : 0));
	}
}
