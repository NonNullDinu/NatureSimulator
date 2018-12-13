package ns.parallelComputing;

import ns.openglObjects.Texture;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class TextureCreateRequest extends Request {

	private final Texture target;
	private final ByteBuffer pixels;

	public TextureCreateRequest(Texture target, ByteBuffer pixels) {
		this.target = target;
		this.pixels = pixels;
	}

	@Override
	public void execute() {
		int id = GL11.glGenTextures();
		target.setId(id);
		target.loadWith(pixels);
	}
}