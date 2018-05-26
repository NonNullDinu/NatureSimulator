package ns.ui.shop;

import org.lwjgl.util.vector.Vector2f;

import ns.components.Blueprint;
import ns.components.BlueprintCreator;
import ns.ui.Button;

public class ShopItem extends Button {
	public static final Vector2f SCALE = new Vector2f(0.1f, 0.1f);
	
	private final Vector2f position;

	private Blueprint blueprint;

	public ShopItem(Vector2f position, Blueprint blueprint) {
		super(position, SCALE);
		this.position = position;
		this.blueprint = blueprint;
	}

	public Vector2f getPosition() {
		return position;
	}
	
	public Blueprint getEntityBlueprint() {
		return blueprint;
	}

	public static ShopItem item(int i) {
		Vector2f position = new Vector2f();
		position.x = -0.9f + ((i % 4) * (SCALE.x + 0.1f));
		position.y = 0.75f - ((i / 4) * (SCALE.y + 0.05f));
		return new ShopItem(position, BlueprintCreator.createModelBlueprintFor(Integer.toString(1000 + i)));
	}
}