package obj;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ns.utils.GU;

public class Material {
	private Vector3f color;
	private Vector3f indicators;
	private String name;
	private Vector4f data;
	private byte index;

	protected Material(byte index) {
		this.index = index;
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

	public byte getIndex() {
		return index;
	}

	public List<Byte> getBytes() {
		byte[][] data = new byte[][] { GU.getBytes(this.color.x), GU.getBytes(this.color.y),
				GU.getBytes(this.color.z) };
		List<Byte> bytes = new ArrayList<>();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 4; j++)
				bytes.add(data[i][j]);
		data = new byte[][] { GU.getBytes(this.indicators.x), GU.getBytes(this.indicators.y),
				GU.getBytes(this.indicators.z) };
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 4; j++)
				bytes.add(data[i][j]);
		data = new byte[][] { GU.getBytes(this.data.x) };
		for (int i = 0; i < 1; i++)
			for (int j = 0; j < 4; j++)
				bytes.add(data[i][j]);
		return bytes;
	}
}
