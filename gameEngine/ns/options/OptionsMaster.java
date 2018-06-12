package ns.options;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import ns.customFileFormat.TexFile;
import ns.fontMeshCreator.GUIText;
import ns.ui.GUIButton;
import ns.utils.GU;

public class OptionsMaster {
	public static Options createOptions() {
		List<Option> options = new ArrayList<>();
		while (GU.Z003 == null)
			Thread.yield();
		options.add(new OnOffOption(new Vector2f(0, 0), new Vector2f(0.1f, 0.1f),
				new GUIText("Some test option On", 1f, GU.Z003, new Vector2f(0, 0.02f), 0.2f, true)));
		GUIButton back = new GUIButton(new Vector2f(-0.95f, -0.95f), new Vector2f(0.05f, 0.05f),
				new TexFile("textures/back_arrow.tex").load());
		return new Options(options, back);
	}
}