package ns.mainMenu;

import ns.entities.Entity;
import ns.interfaces.UIMenu;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class MainMenu implements UIMenu {
	public static MainMenu instance;
	private List<MainMenuButton> buttons;
	private Entity DNA;
	private Vector4f dnaLocation;

	public MainMenu(List<MainMenuButton> buttons, Entity DNA, Vector4f dnaLocation) {
		instance = this;
		this.buttons = buttons;
		this.DNA = DNA;
		this.dnaLocation = dnaLocation;
	}

	public void update() {
		int idx = 0;
		for (MainMenuButton button : buttons) {
			if (button.isMouseOver()) {
				DNA.setRotY(idx * 36f);
				if (Mouse.isButtonDown(0)) {
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

	public Vector4f getDnaLocation() {
		return dnaLocation;
	}
}