package ns.utils;

public class Vector2b {
	public boolean x, y;

	public Vector2b() {
	}

	public Vector2b(boolean x, boolean y) {
		this.x = x;
		this.y = y;
	}

	public Vector2b(Vector2b src) {
		x = src.x;
		y = src.y;
	}
}