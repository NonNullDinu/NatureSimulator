package ns.terrain;

import org.lwjgl.util.vector.Vector3f;

public class TerrainVertex {
	protected Vector3f position;
	protected Vector3f color;
	
	public TerrainVertex(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
}