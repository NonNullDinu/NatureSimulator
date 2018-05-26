package ns.ui.shop;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import ns.renderers.GUIRenderer;
import ns.ui.ComplexGUI;

public class ShopMaster {
	public static Shop shop;
	
	public static Shop createShop(GUIRenderer guiRenderer) {
		List<ShopItem> items = new ArrayList<>();
		ComplexGUI complex = new ComplexGUI(new Vector2f(-0.6f, 0f), new Vector2f(0.4f, 0.9f), null, guiRenderer);
		for(int i = 0; i < 2; i++)
			items.add(ShopItem.item(i, complex));
		Shop s = new Shop(items, complex);
		shop = s;
		return s;
	}
}