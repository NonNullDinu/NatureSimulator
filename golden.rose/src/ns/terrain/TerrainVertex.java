package ns.terrain;

import org.lwjgl.util.vector.Vector3f;

class TerrainVertex {
	final Vector3f position;
	Vector3f color;

	public TerrainVertex(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
}