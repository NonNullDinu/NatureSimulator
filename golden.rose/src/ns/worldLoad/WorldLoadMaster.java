package ns.worldLoad;

import data.SaveData;
import ns.fontMeshCreator.GUIText;
import ns.fontRendering.TextMaster;
import ns.ui.Button;
import ns.utils.GU;
import ns.world.World;
import ns.worldSave.LoadWorldMaster;
import org.lwjgl.util.vector.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldLoadMaster {
	public static int count;
	private static List<GUIText> texts;
	private static List<Button> buttons;

	public static void buildUI() {
		texts = new ArrayList<>();
		buttons = new ArrayList<>();
		try {
			count = Integer.parseInt(new String(SaveData.getResourceAt("saves.dir").asInputStream().readAllBytes()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(count);
		for (int i = 0; i < count; i++) {
			GUIText text = new GUIText("Save " + (i + 1), 1f, GU.Z003, new Vector2f(0f, 0.5f - 0.1f * i), 0.3f, true);
			texts.add(text);
			TextMaster.loadText(text);
			text.setColour(1, 1, 1);
			buttons.add(new Button(new Vector2f(0f, 0.5f - 0.1f * i), new Vector2f(0.1f, 0.05f)));
		}
	}

	public static void updateCountAndUI() {
		texts = new ArrayList<>();
		buttons = new ArrayList<>();
		try {
			count = Integer.parseInt(new String(SaveData.getResourceAt("saves.dir").asInputStream().readAllBytes()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(count);
		for (int i = 0; i < count; i++) {
			GUIText text = new GUIText("Save " + (i + 1), 1f, GU.Z003, new Vector2f(0f, 0.5f - 0.1f * i), 0.3f, true);
			texts.add(text);
			TextMaster.loadText(text);
			text.setColour(1, 1, 1);
			buttons.add(new Button(new Vector2f(0f, 0.5f - 0.1f * i), new Vector2f(0.1f, 0.05f)));
		}
	}

	public static World returnSelected() {
		int i = 0;
		for (Button button : buttons) {
			if (button.clicked()) {
				return LoadWorldMaster.loadWorld(SaveData.getResourceAt("save" + i + "." + GU.WORLD_SAVE_FILE_FORMAT, true));
			}
			i++;
		}
		return null;
	}

	public static void renderUI() {
		for (GUIText text : texts)
			TextMaster.add(text);
		TextMaster.render();
		for (GUIText text : texts)
			TextMaster.remove(text);
	}
}