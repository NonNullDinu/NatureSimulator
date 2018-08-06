package ns.options;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import ns.interfaces.Action;
import ns.interfaces.UIMenu;
import ns.renderers.GUIRenderer;
import ns.ui.GUIButton;
import ns.utils.GU;

public class Options implements UIMenu {
	private List<Option> options;
	private GUIButton back;
	private Action action;

	public Options(List<Option> options, GUIButton back, Action backClicked) {
		this.options = options;
		this.back = back;
		this.action = backClicked;
	}

	public void update() {
		Vector2f mp = GU.normalizedMousePos();
		for (Option option : options) {
			Vector2f pos = option.loc(mp);
			option.checkAndClick(pos);
		}
		if (back.clicked()) {
			action.execute();
		}
	}

	public void render() {
		for (Option option : options) {
			option.render();
		}
		GUIRenderer.instance.render(back);
	}
}