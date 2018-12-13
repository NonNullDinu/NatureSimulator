package ns.options;

import ns.fontMeshCreator.GUIText;
import ns.fontRendering.TextMaster;
import ns.utils.GU;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public abstract class Option {
	final GUIText text;
	private final Vector2f position;
	private final Vector2f scale;

	public Option(Vector2f position, Vector2f scale, GUIText text) {
		this.position = position;
		this.scale = scale;
		this.text = text;
		TextMaster.loadText(text);
	}

	void checkAndClick(Vector2f pos) {
		if (Math.abs(pos.x) <= scale.x && Math.abs(pos.y) <= scale.y && Mouse.isButtonDown(0) && !GU.prevFrameClicked)
			click(pos.x - scale.x, pos.y - scale.y);
	}

	protected abstract void click(float x, float y);

	Vector2f loc(Vector2f mouse) {
		return Vector2f.sub(position, new Vector2f(mouse.x, mouse.y), null);
	}

	public abstract void render();
}