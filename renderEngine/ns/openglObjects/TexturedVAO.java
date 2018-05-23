package ns.openglObjects;

public class TexturedVAO extends VAO {

	private Texture texture;

	public TexturedVAO(Texture texture, VAO vao) {
		super(vao.getId(), vao.getVertexCount(), vao.getBuffers(), vao.hasIndices());
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}
	
	@Override
	public void render() {
		bind();
		batchRenderCall();
		unbind();
	}
	
	@Override
	public void bind() {
		super.bind();
		texture.bindToTextureUnit(0);
	}
}
