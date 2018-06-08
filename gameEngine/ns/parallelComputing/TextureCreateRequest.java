package ns.parallelComputing;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import ns.openglObjects.Texture;

public class TextureCreateRequest extends Request {

	private Texture target;
	private ByteBuffer pixels;

	public TextureCreateRequest(Texture target, ByteBuffer pixels) {
		super("create texture", null);
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