package ns.shaders;

import org.lwjgl.opengl.GL20;

public class UniformInt extends UniformVar {

	public UniformInt(int location) {
		super(location);
	}
	
	public void load(int value) {
		GL20.glUniform1i(location, value);
	}
}
