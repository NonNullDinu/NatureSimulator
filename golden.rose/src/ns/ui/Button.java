package ns.ui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class Button {
	private final Vector2f center;
	private final Vector2f scale;

	public Button(Vector2f center, Vector2f scale) {
		this.center = center;
		this.scale = scale;
	}

	public boolean isMouseOver() {
		double normalizedX = -1.0 + 2.0 * (double) Mouse.getX() / Display.getWidth();
		double normalizedY = -(1.0 - 2.0 * (double) Mouse.getY() / Display.getHeight());
		Vector2f relToButtonLocation = Vector2f.sub(center, new Vector2f((float) normalizedX, (float) normalizedY), null);
		return (Math.abs(relToButtonLocation.x) <= scale.x && Math.abs(relToButtonLocation.y) <= scale.y);
	}

	public boolean clicked() {
		return isMouseOver() && Mouse.isButtonDown(0);
	}

	public Vector2f getCenter() {
		return center;
	}

	public Vector2f getScale() {
		return scale;
	}
}