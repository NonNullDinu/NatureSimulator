package ns.shaders;

import org.lwjgl.opengl.GL20;

public class UniformFloat extends UniformVar {

	public UniformFloat(int location) {
		super(location);
	}
	
	public void load(float value) {
		GL20.glUniform1f(location, value);
	}
}
