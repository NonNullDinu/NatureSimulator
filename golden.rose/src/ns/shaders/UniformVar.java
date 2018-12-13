package ns.shaders;

public abstract class UniformVar {
	static final int TYPE_FLOAT = 1;
	static final int TYPE_VEC2 = 2;
	static final int TYPE_VEC3 = 3;
	static final int TYPE_VEC4 = 4;
	static final int TYPE_MAT4 = 5;
	static final int TYPE_INT = 6;
	static final int TYPE_BOOL = 7;
	static final int TYPE_SAMPLER_2D = 8;
	static final int TYPE_LIGHT = 9;
	private final String name;
	int location;

	public UniformVar(int location, String name) {
		this.location = location;
		this.name = name;
	}

	public UniformVar(String name) {
		this.name = name;
	}

	public void loadLocation(UniformLocator locator) {
		this.location = locator.getLocation(name);
	}
}
