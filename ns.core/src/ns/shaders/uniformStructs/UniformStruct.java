package ns.shaders.uniformStructs;

import ns.shaders.UniformLocator;
import ns.shaders.UniformVar;

import java.util.List;

public abstract class UniformStruct extends UniformVar {

	private final String name;
	private final List<UniformVar> attributes;

	UniformStruct(String name, List<UniformVar> attributes) {
		super(-1, name);
		this.name = name;
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	protected void load(List<UniformValue> values) {
		int itn = Math.min(attributes.size(), values.size());
		for (int i = 0; i < itn; i++) {
			values.get(i).loadTo(attributes.get(i));
		}
	}

	void load(UniformValue... values) {
		int itn = Math.min(attributes.size(), values.length);
		for (int i = 0; i < itn; i++) {
			values[i].loadTo(attributes.get(i));
		}
	}

	@Override
	public void loadLocation(UniformLocator locator) {
		for (UniformVar attribute : attributes)
			attribute.loadLocation(locator);
	}
}