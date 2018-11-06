package ns.ui;

import ns.openglObjects.Texture;
import org.lwjgl.util.vector.Vector2f;

public class GUIButton extends Button {

	private final Texture texture;

	public GUIButton(Vector2f center, Vector2f scale, Texture tex) {
		super(center, scale);
		this.texture = tex;
	}

	public Texture getTexture() {
		return texture;
	}

//	@Override
//	public void render(GUIRenderer renderer, float[] args, int... argType) {
//		int i = 0;
//		float alpha = 1;
//		for(int arg : argType) {
//			if (arg == GUI.ALPHA) {
//				alpha = args[i];
//			}
//			i++;
//		}
//		renderer.render();
//	}
}