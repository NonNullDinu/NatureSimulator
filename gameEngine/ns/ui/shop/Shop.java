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
	private int d;
	private boolean increasing;

	public Shop(List<ShopItem> items, ComplexGUI complex) {
		this.items = items;
		state = SS.OPEN;
		this.complex = complex;
	}

	public Entity update() {
		if (d > 0) {
			if (increasing) {
				d += 2;
				increasing = d < 20;
			} else
				d -= 2;
			GU.setMouseCursor(GU.createCursor(0, 63, 1,
					GU.getMouseTexture(currentlySelected.getEntityBlueprint().getFolder(), d), null, d));
		}
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
			GU.setMouseCursor(null);
		}
		if (state == SS.CLOSED)
			return null;
		if (state == SS.OPEN) {
			for (ShopItem item : items) {
				if (item.clicked()) {
					currentlySelected = item;
					state = SS.BUYING;
					complex.getCenter().x -= SLIDE_OFFSET;
					GU.setMouseCursor(GU.createCursor(0, 63, 1,
							GU.getMouseTexture(item.getEntityBlueprint().getFolder(), 0), null));
					return null;
				}
			}
			if (Mouse.isButtonDown(1) && GU.mouseDelta.length() == 0f && GU.lastFramesLengths == 0f) {
				state = SS.CLOSED;
				GU.setMouseCursor(null);
			}
		}
		if (state == SS.BUYING) {
			if (Mouse.isButtonDown(0) && !GU.prevFrameClicked && GU.mouseDelta.length() == 0f
					&& GU.lastFramesLengths == 0f) {
				GU.setMouseCursor(GU.createCursor(0, 63, 1,
						GU.getMouseTexture(currentlySelected.getEntityBlueprint().getFolder(), 2), null, 2));
				d = 2;
				increasing = true;
				return new Entity(
						BlueprintCreator.createBlueprintFor(currentlySelected.getEntityBlueprint().getFolder()),
						MousePicker.getCurrentTerrainPoint());
			} else if (Mouse.isButtonDown(1) && GU.mouseDelta.length() == 0f && GU.lastFramesLengths == 0f) {
				complex.getCenter().x += SLIDE_OFFSET;
				state = SS.CLOSED;
				GU.setMouseCursor(null);
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