package ns.ui.shop;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import ns.entities.Entity;

public class Shop {
	private List<ShopItem> items;
	private ShopItem currentlySelected;
	private SS state;

	public Shop(List<ShopItem> items) {
		this.items = items;
		state = SS.OPEN;
	}

	public Entity update() {
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
			if (!Mouse.isButtonDown(1))
				return new Entity(currentlySelected.getEntityBlueprint(), new Vector3f());
		}
		return null;
	}

	public List<ShopItem> getItems() {
		return items;
	}

	public boolean open() {
		return state == SS.OPEN;
	}
}