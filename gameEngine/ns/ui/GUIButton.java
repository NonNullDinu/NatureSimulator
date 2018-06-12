package ns.ui;

import org.lwjgl.util.vector.Vector2f;

import ns.openglObjects.Texture;

public class GUIButton extends Button {

	private Texture texture;

	public GUIButton(Vector2f center, Vector2f scale, Texture tex) {
		super(center, scale);
		this.texture = tex;
	}

	public Texture getTexture() {
		return texture;
	}
}