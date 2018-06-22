package ns.derrivedOpenGLObjects;

import org.lwjgl.util.vector.Vector2f;

import ns.openglObjects.Texture;

public class FlareTexture {
	private Vector2f position;
	private float scale;
	private Texture texture;
	private boolean hasRotation;
	private float rotation;
	private float rotOff;

	public FlareTexture(Texture texture, float scale) {
		this.texture = texture;
		this.scale = scale;
		this.hasRotation = false;
	}

	public FlareTexture(Texture texture, float scale, float rotOff) {
		this.texture = texture;
		this.scale = scale;
		this.hasRotation = true;
		this.rotation = 0;
		this.rotOff = rotOff;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation + rotOff;
	}

	public boolean hasRotation() {
		return hasRotation;
	}
}