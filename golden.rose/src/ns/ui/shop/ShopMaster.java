/*
 * Copyright (C) 2018  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.ui.shop;

import ns.renderers.GUIRenderer;
import ns.ui.ComplexGUI;
import ns.utils.GU;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class ShopMaster {
	public static Shop shop;

	public static Shop createShop(GUIRenderer guiRenderer) {
		List<ShopItem> items = new ArrayList<>();
		ComplexGUI complex = new ComplexGUI(new Vector2f(-0.6f, 0f), new Vector2f(0.4f, 0.9f), null, guiRenderer);
		for (int i = 0; i < GU.TOTAL_NUMBER_OF_ENTITIES; i++)
			items.add(ShopItem.item(i, complex));
		Shop s = new Shop(items, complex);
		shop = s;
		return s;
	}
}