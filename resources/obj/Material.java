package obj;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Material {
	private Vector3f color;
	private Vector3f indicators;
	private String name;
	private Vector4f data;
	
	protected Material() {
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setIndicators(Vector3f indicators) {
		this.indicators = indicators;
	}

	public Vector3f getColor() {
		return color;
	}

	public Vector3f getIndicators() {
		return indicators;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setData(Vector4f data) {
		this.data = data;
	}

	public Vector4f getData() {
		return data;
	}
}
