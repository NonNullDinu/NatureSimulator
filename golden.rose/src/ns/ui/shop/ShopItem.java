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

import ns.components.Blueprint;
import ns.components.BlueprintCreator;
import ns.ui.Button;
import ns.ui.ComplexGUI;
import org.lwjgl.util.vector.Vector2f;

public class ShopItem extends Button {
	public static final Vector2f SCALE = new Vector2f(0.1f, 0.1f);

	private final Vector2f position;
	private final Blueprint blueprint;
	private final ComplexGUI complex;

	public ShopItem(Vector2f position, Blueprint blueprint, ComplexGUI complex) {
		super(position, SCALE);
		this.position = position;
		this.blueprint = blueprint;
		this.complex = complex;
	}

	public static ShopItem item(int i, ComplexGUI complex) {
		Vector2f position = new Vector2f();
		position.x = -0.3f + ((i % 4) * (SCALE.x * 2.0f));
		position.y = 0.75f - ((i / 4) * (SCALE.y * 2.0f));
		return new ShopItem(position, BlueprintCreator.createModelBlueprintFor(Integer.toString(1000 + i)), complex);
	}

	public Vector2f getPosition() {
		return position;
	}

	public Blueprint getEntityBlueprint() {
		return blueprint;
	}

	@Override
	public boolean isMouseOver() {
		Vector2f center = new Vector2f(super.getCenter());
		Vector2f.add(super.getCenter(), complex.getCenter(), super.getCenter());
		boolean answer = super.isMouseOver();
		super.getCenter().set(center);
		return answer;
	}
}