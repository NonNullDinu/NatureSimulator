package ns.shaders;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class UniformMat4 extends UniformVar {
	
	private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
	
	public UniformMat4(String name) {
		super(name);
	}
	
	public void load(Matrix4f value) {
		value.store(matrix);
		matrix.flip();
		GL20.glUniformMatrix4(location, false, matrix);
	}
}
