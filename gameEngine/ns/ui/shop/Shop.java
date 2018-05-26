package ns.ui.shop;

import java.util.List;

import org.lwjgl.input.Mouse;

import ns.components.BlueprintCreator;
import ns.entities.Entity;
import ns.ui.ComplexGUI;
import ns.utils.GU;
import ns.utils.MousePicker;

public class Shop {
	private static final float SLIDE_OFFSET = 0.7f;
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
		if (GU.Key.KEY_S.isPressed() && !GU.Key.KEY_S.pressedPreviousFrame()) {
			if (state != SS.OPEN) {
				if (state == SS.BUYING)
					complex.getCenter().x += SLIDE_OFFSET;
				state = SS.OPEN;
			} else {
				if (state == SS.BUYING)
					complex.getCenter().x += SLIDE_OFFSET;
				state = SS.CLOSED;
			}
			currentlySelected = null;
		}
		if (state == SS.CLOSED)
			return null;
		if (state == SS.OPEN)
			for (ShopItem item : items) {
				if (item.clicked()) {
					currentlySelected = item;
					state = SS.BUYING;
					complex.getCenter().x -= SLIDE_OFFSET;
					return null;
				}
			}
		if (state == SS.BUYING) {
			if (Mouse.isButtonDown(0) && !GU.prevFrameClicked)
				return new Entity(
						BlueprintCreator.createBlueprintFor(currentlySelected.getEntityBlueprint().getFolder()),
						MousePicker.getCurrentTerrainPoint());
			else if (Mouse.isButtonDown(1) && GU.mouseDelta.length() == 0f && GU.lastFramesLengths == 0f) {
				complex.getCenter().x += SLIDE_OFFSET;
				state = SS.CLOSED;
			}
		}
		return null;
	}

	public List<ShopItem> getItems() {
		return items;
	}

	public boolean open() {
		return state != SS.CLOSED;
	}

	public ComplexGUI getComplex() {
		return complex;
	}
}