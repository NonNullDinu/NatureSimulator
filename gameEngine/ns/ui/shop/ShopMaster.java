package ns.ui.shop;

import java.util.ArrayList;
import java.util.List;

public class ShopMaster {
	public static Shop shop;
	
	public static Shop createShop() {
		List<ShopItem> items = new ArrayList<>();
		for(int i = 0; i < 2; i++)
			items.add(ShopItem.item(i));
		Shop s = new Shop(items);
		shop = s;
		return s;
	}
}