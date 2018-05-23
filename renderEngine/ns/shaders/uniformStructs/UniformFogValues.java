package ns.shaders.uniformStructs;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import ns.shaders.UniformLocator;
import ns.shaders.UniformVar;

public class UniformFogValues extends UniformStruct {

	public UniformFogValues(String name, UniformLocator locator) {
		super(name, getAttributes(name, locator));
	}

	private static List<UniformVar> getAttributes(String name, UniformLocator locator) {
		List<UniformVar> attributes = new ArrayList<>();
		attributes.add(locator.locateUniformFloat(name + ".density", false));
		attributes.add(locator.locateUniformFloat(name + ".gradient", false));
		return attributes;
	}
	
	public void load(Vector2f values) {
		super.load(new UniformValue(values.x), new UniformValue(values.y));
	}
}