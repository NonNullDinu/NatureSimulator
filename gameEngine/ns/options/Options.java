package ns.options;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import ns.mainEngine.GS;
import ns.mainEngine.MainGameLoop;
import ns.renderers.GUIRenderer;
import ns.ui.GUIButton;
import ns.utils.GU;

public class Options {
	private List<Option> options;
	private GUIButton back;

	public Options(List<Option> options, GUIButton back) {
		this.options = options;
		this.back = back;
	}
	
	public void update() {
		Vector2f mp = GU.normalizedMousePos();
		for(Option option : options) {
			Vector2f pos = option.loc(mp);
			option.checkAndClick(pos);
		}
		if(back.clicked()) {
			MainGameLoop.state = GS.MENU;
		}
	}

	public void render() {
		for(Option option : options) {
			option.render();
		}
		GUIRenderer.instance.render(back);
	}
}