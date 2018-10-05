package ns.entitySpot;

import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import org.lwjgl.util.vector.Vector3f;

public class MovingEntitySpot {
	private Vector3f position;
	private VAO model;
	private Texture placeableTexture, notPlaceableTexture;

	public MovingEntitySpot(Vector3f position, VAO model, Texture placeableTexture, Texture notPlaceableTexture) {
		this.position = position;
		this.model = model;
		this.placeableTexture = placeableTexture;
		this.notPlaceableTexture = notPlaceableTexture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public VAO getModel() {
		return model;
	}

	public Texture getPlaceableTexture() {
		return placeableTexture;
	}

	public Texture getNotPlaceableTexture() {
		return notPlaceableTexture;
	}
}