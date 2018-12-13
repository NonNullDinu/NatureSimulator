package ns.shaders.uniformStructs;

import ns.shaders.UniformFloat;
import ns.shaders.UniformLocator;
import ns.shaders.UniformVar;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class UniformFogValues extends UniformStruct {

	public UniformFogValues(String name, UniformLocator locator) {
		super(name, getAttributes(name, locator));
	}

	private static List<UniformVar> getAttributes(String name, UniformLocator locator) {
		List<UniformVar> attributes = new ArrayList<>();
		attributes.add(new UniformFloat(name + ".density"));
		attributes.add(new UniformFloat(name + ".gradient"));
		return attributes;
	}

	public void load(Vector2f values) {
		super.load(new UniformValue(values.x), new UniformValue(values.y));
	}
}