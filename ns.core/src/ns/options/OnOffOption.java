package ns.options;

import ns.fontMeshCreator.GUIText;
import ns.fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector2f;

public class OnOffOption extends Option {
	private boolean on;
	
	public OnOffOption(Vector2f position, Vector2f scale, GUIText text) {
		super(position, scale, text);
	}

	@Override
	protected void click(float x, float y) {
		TextMaster.delete(text);
		text.setText(text.getTextString().replace((on ? "On" : "Off"), (!on ? "On" : "Off")));
		on = !on;
		TextMaster.loadText(text);
	}

	@Override
	public void render() {
		TextMaster.add(text);
		TextMaster.render();
		TextMaster.remove(text);
	}
}