package ns.mainMenu;

import ns.interfaces.Action;
import ns.openglObjects.Texture;
import ns.ui.Button;
import org.lwjgl.util.vector.Vector2f;

public class MainMenuButton extends Button {
	private final Action action;
	private final Texture tex;

	public MainMenuButton(Vector2f center, Vector2f scale, Action action, Texture texture) {
		super(center, scale);
		this.action = action;
		this.tex = texture;
	}

	public void update() {
		if (super.clicked())
			action.execute();
	}

	public Texture getTex() {
		return tex;
	}

	public void executeAction() {
		action.execute();
	}
}