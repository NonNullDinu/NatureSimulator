package ns.ui;

import ns.openglObjects.Texture;
import org.lwjgl.util.vector.Vector2f;

public class GUITexture {
	private Vector2f center;
	private Vector2f scale;
	private final Texture texture;

	public GUITexture(Vector2f center, Vector2f scale, Texture texture) {
		this.center = center;
		this.scale = scale;
		this.texture = texture;
	}

	public Vector2f getCenter() {
		return center;
	}

	public void setCenter(Vector2f center) {
		this.center = center;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
	public Texture getTexture() {
		return texture;
	}
}