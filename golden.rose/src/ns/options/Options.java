package ns.options;

import ns.interfaces.Action;
import ns.interfaces.UIMenu;
import ns.renderers.GUIRenderer;
import ns.ui.GUIButton;
import ns.utils.GU;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class Options implements UIMenu {
	private final List<Option> options;
	private final GUIButton back;
	private final Action action;

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