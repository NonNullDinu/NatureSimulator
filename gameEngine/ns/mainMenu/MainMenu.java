package ns.mainMenu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import ns.entities.Entity;

public class MainMenu {
	public static MainMenu instance;
	private List<MainMenuButton> buttons = new ArrayList<>();
	private Entity DNA;
	
	public MainMenu(List<MainMenuButton> buttons, Entity DNA) {
		instance = this;
		this.buttons = buttons;
		this.DNA = DNA;
	}
	
	public void update() {
		int idx = 0;
		for(MainMenuButton button : buttons) {
			if(button.isMouseOver()) {
				DNA.setRotY(idx * 36f);
				if(Mouse.isButtonDown(0)) {
					button.executeAction();
				}
			}
			idx++;
		}
	}

	public List<MainMenuButton> getButtons() {
		return buttons;
	}

	public Entity getDNA() {
		return DNA;
	}
}