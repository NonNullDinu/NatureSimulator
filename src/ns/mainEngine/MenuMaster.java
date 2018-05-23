package ns.mainEngine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.components.Blueprint;
import ns.components.BlueprintCreator;
import ns.customFileFormat.TexFile;
import ns.entities.Entity;
import ns.mainMenu.MainMenu;
import ns.mainMenu.MainMenuButton;
import ns.ui.Action;

public class MenuMaster {
	public static MainMenu menu;

	public static MainMenu createMainMenu() {
		List<MainMenuButton> buttons = new ArrayList<>();
		buttons.add(new MainMenuButton(new Vector2f(0f, 0.5f), new Vector2f(0.1f, 0.05f), new Action() {
			@Override
			public void execute() {
				MainGameLoop.state = GS.GAME;
			}
		}, new TexFile("res/textures/buttonTextures/mainMenu_Start.tex").load()));
		Blueprint dnaBlueprint = BlueprintCreator.createBlueprintFor("menuDNA");
		menu = new MainMenu(buttons, new Entity(dnaBlueprint, new Vector3f(1f, -5f, -8.1f)));
		return menu;
	}
}