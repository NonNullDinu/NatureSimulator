package ns.shaders.uniformStructs;

import java.util.ArrayList;
import java.util.List;

import ns.entities.Light;
import ns.shaders.UniformLocator;
import ns.shaders.UniformVar;
import ns.shaders.UniformVec2;
import ns.shaders.UniformVec3;

public class UniformLight extends UniformStruct {

	public UniformLight(String name, UniformLocator locator) {
		super(name, getAttributes(name, locator));
	}

	public void load(Light light) {
		super.load(new UniformValue(light.dir), new UniformValue(light.color), new UniformValue(light.bias));
	}

	private static List<UniformVar> getAttributes(String name, UniformLocator locator) {
		List<UniformVar> attributes = new ArrayList<>();
		attributes.add(new UniformVec3(name + ".direction"));
		attributes.add(new UniformVec3(name + ".color"));
		attributes.add(new UniformVec2(name + ".bias"));
		return attributes;
	}
}