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
	private String name;

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
