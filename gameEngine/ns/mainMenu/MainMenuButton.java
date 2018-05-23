package ns.mainMenu;

import org.lwjgl.util.vector.Vector2f;

import ns.openglObjects.Texture;
import ns.ui.Action;
import ns.ui.Button;

public class MainMenuButton extends Button {
	private Action action;
	private Texture tex;

	public MainMenuButton(Vector2f center, Vector2f scale, Action action, Texture texture) {
		super(center, scale);
		this.action = action;
		this.tex = texture;
	}
	
	public void update() {
		if(super.clicked())
			action.execute();
	}

	public Texture getTex() {
		return tex;
	}

	public void executeAction() {
		action.execute();
	}
}