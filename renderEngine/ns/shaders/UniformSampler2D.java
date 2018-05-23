package ns.shaders;

import org.lwjgl.opengl.GL20;

public class UniformSampler2D extends UniformVar {

	public UniformSampler2D(int location) {
		super(location);
	}
	
	public void load(int value) {
		GL20.glUniform1i(location, value);
	}
}