package ns.ui;

import ns.openglObjects.Texture;
import org.lwjgl.util.vector.Vector2f;

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