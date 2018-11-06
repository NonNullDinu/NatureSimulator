package ns.shaders.uniformStructs;

import ns.shaders.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

class UniformValue {

	private static final int INT = 1;
	private static final int FLOAT = 2;
	private static final int BOOLEAN = 3;
	private static final int VEC2 = 4;
	private static final int VEC3 = 5;
	private static final int VEC4 = 6;
	private static final int MAT4 = 7;

	private int int_value;
	private float float_value;
	private boolean boolean_value;
	private Vector2f vec2_value;
	private Vector3f vec3_value;
	private Vector4f vec4_value;
	private Matrix4f mat4_value;

	private final int type;

	public UniformValue(int value) {
		this.int_value = value;
		this.type = INT;
	}

	public UniformValue(float value) {
		this.float_value = value;
		this.type = FLOAT;
	}

	public UniformValue(boolean value) {
		this.boolean_value = value;
		this.type = BOOLEAN;
	}

	public UniformValue(Vector2f value) {
		this.vec2_value = value;
		this.type = VEC2;
	}

	public UniformValue(Vector3f value) {
		this.vec3_value = value;
		this.type = VEC3;
	}

	public UniformValue(Vector4f value) {
		this.vec4_value = value;
		this.type = VEC4;
	}

	public UniformValue(Matrix4f value) {
		this.mat4_value = value;
		this.type = MAT4;
	}

	public void loadTo(UniformVar var) {
		switch (type) {
		case INT:
			if (var instanceof UniformInt)
				((UniformInt) var).load(int_value);
			if (var instanceof UniformSampler2D)
				((UniformSampler2D) var).load(int_value);
			break;
		case FLOAT:
			((UniformFloat) var).load(float_value);
			break;
		case BOOLEAN:
			((UniformBool) var).load(boolean_value);
			break;
		case VEC2:
			((UniformVec2) var).load(vec2_value);
			break;
		case VEC3:
			((UniformVec3) var).load(vec3_value);
			break;
		case VEC4:
			((UniformVec4) var).load(vec4_value);
			break;
		case MAT4:
			((UniformMat4) var).load(mat4_value);
			break;
		}
	}
}