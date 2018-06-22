package ns.derrivedOpenGLObjects;

import ns.openglObjects.Texture;
import ns.openglObjects.VAO;

public class TexturedVAO extends VAO {

	private Texture texture;

	public TexturedVAO(Texture texture, VAO vao) {
		super(vao.getID(), vao.getVertexCount(), vao.getBuffers(), vao.hasIndices());
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}
	
	@Override
	public void render() {
		bind((Integer[]) super.getBuffers().keySet().toArray());
		batchRenderCall();
		unbind();
	}
	
	@Override
	public void bind(int... attributes) {
		super.bind(attributes);
		texture.bindToTextureUnit(0);
	}
}
