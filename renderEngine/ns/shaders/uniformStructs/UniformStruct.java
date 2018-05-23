package ns.shaders.uniformStructs;

import java.util.List;

import ns.shaders.UniformVar;

public abstract class UniformStruct {

	private String name;
	private List<UniformVar> attributes;

	public UniformStruct(String name, List<UniformVar> attributes) {
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

	protected void load(UniformValue... values) {
		int itn = Math.min(attributes.size(), values.length);
		for (int i = 0; i < itn; i++) {
			values[i].loadTo(attributes.get(i));
		}
	}
}