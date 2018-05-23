package ns.shaders.uniformStructs;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ns.shaders.UniformBool;
import ns.shaders.UniformFloat;
import ns.shaders.UniformInt;
import ns.shaders.UniformMat4;
import ns.shaders.UniformSampler2D;
import ns.shaders.UniformVar;
import ns.shaders.UniformVec2;
import ns.shaders.UniformVec3;
import ns.shaders.UniformVec4;

public class UniformValue {

	protected static final int INT = 1;
	protected static final int FLOAT = 2;
	protected static final int BOOLEAN = 3;
	protected static final int VEC2 = 4;
	protected static final int VEC3 = 5;
	protected static final int VEC4 = 6;
	protected static final int MAT4 = 7;

	protected int int_value;
	protected float float_value;
	protected boolean boolean_value;
	protected Vector2f vec2_value;
	protected Vector3f vec3_value;
	protected Vector4f vec4_value;
	protected Matrix4f mat4_value;

	protected final int type;

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