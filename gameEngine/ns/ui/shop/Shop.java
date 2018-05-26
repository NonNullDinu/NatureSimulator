package ns.ui.shop;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import ns.components.BlueprintCreator;
import ns.entities.Entity;
import ns.ui.ComplexGUI;
import ns.utils.GU;
import ns.utils.MousePicker;

public class Shop {
	private List<ShopItem> items;
	private ShopItem currentlySelected;
	private SS state;
	private ComplexGUI complex;

	public Shop(List<ShopItem> items, ComplexGUI complex) {
		this.items = items;
		state = SS.OPEN;
		this.complex = complex;
	}

	public Entity update() {
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if(state != SS.OPEN)
				state = SS.OPEN;
			else
				state = SS.CLOSED;
			currentlySelected = null;
		}
		if (state == SS.CLOSED)
			return null;
		if (state == SS.OPEN)
			for (ShopItem item : items) {
				if (item.clicked()) {
					currentlySelected = item;
					state = SS.BUYING;
				}
			}
		if (state == SS.BUYING) {
			if (Mouse.isButtonDown(0) && !GU.prevFrameClicked)
				return new Entity(
						BlueprintCreator.createBlueprintFor(currentlySelected.getEntityBlueprint().getFolder()),
						MousePicker.getCurrentTerrainPoint());
			else if (Mouse.isButtonDown(1))
				state = SS.CLOSED;
		}
		return null;
	}

	public List<ShopItem> getItems() {
		return items;
	}

	public boolean open() {
		return state == SS.OPEN;
	}

	public ComplexGUI getComplex() {
		return complex;
	}
}