package obj;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.ByteBuffer;

public class ByteMaterial {
	private final float[] dataf;

	public ByteMaterial(ByteBuffer buffer) {
		this.dataf = new float[buffer.capacity() / 4];
		for(int i = 0; i < dataf.length; i++)
			dataf[i] = buffer.getFloat();
	}
	
	public Vector3f getColor() {
		return new Vector3f(dataf[0], dataf[1], dataf[2]);
	}
	
	public Vector3f getIndicators() {
		return new Vector3f(dataf[3], dataf[4], dataf[5]);
	}
	
	public Vector4f getData() {
		return new Vector4f(dataf[6], 0, 0, 0);
	}
}