package ns.shaders;

public abstract class UniformVar {
	protected static final int TYPE_FLOAT = 1;
	protected static final int TYPE_VEC2 = 2;
	protected static final int TYPE_VEC3 = 3;
	protected static final int TYPE_VEC4 = 4;
	protected static final int TYPE_MAT4 = 5;
	protected static final int TYPE_INT = 6;
	protected static final int TYPE_BOOL = 7;
	protected static final int TYPE_SAMPLER_2D = 8;
	protected static final int TYPE_LIGHT = 9;

	protected int location;

	public UniformVar(int location) {
		this.location = location;
	}

	public static UniformVar createVar(int type, int location) {
		if (type == TYPE_FLOAT)
			return new UniformFloat(location);
		else if (type == TYPE_VEC2)
			return new UniformVec2(location);
		else if (type == TYPE_VEC3)
			return new UniformVec3(location);
		else if (type == TYPE_VEC4)
			return new UniformVec4(location);
		else if (type == TYPE_MAT4)
			return new UniformMat4(location);
		else if (type == TYPE_INT)
			return new UniformInt(location);
		else if (type == TYPE_BOOL)
			return new UniformBool(location);
		else if (type == TYPE_SAMPLER_2D)
			return new UniformSampler2D(location);
		else
			return null;
	}
}
