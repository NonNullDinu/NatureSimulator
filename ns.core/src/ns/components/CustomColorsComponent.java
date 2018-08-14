package ns.components;

import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class CustomColorsComponent implements IComponent {
	private List<Vector3f> colors;

	public CustomColorsComponent(List<Vector3f> colors) {
		this.colors = colors;
	}

	public List<Vector3f> getColors() {
		return colors;
	}
}