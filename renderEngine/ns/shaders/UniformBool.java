package ns.shaders;

import org.lwjgl.opengl.GL20;

public class UniformBool extends UniformVar {

	public UniformBool(int location) {
		super(location);
	}
	
	public void load(boolean value) {
		GL20.glUniform1i(location, (value ? 1 : 0));
	}
}
