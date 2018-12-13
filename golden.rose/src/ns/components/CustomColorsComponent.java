package ns.components;

import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class CustomColorsComponent implements IComponent {
	private static final long serialVersionUID = 3019699690479243107L;

	private final List<Vector3f> colors;

	CustomColorsComponent(List<Vector3f> colors) {
		this.colors = colors;
	}

	public List<Vector3f> getColors() {
		return colors;
	}

	@Override
	public IComponent copy() {
		return new CustomColorsComponent(colors);
	}
}