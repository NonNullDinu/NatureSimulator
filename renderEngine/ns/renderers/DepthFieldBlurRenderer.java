package ns.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import ns.exceptions.FBOAttachmentException;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import ns.shaders.DepthFieldBlurShader;

public class DepthFieldBlurRenderer extends EffectRenderer {
	private DepthFieldBlurShader shader;
	private Texture bluredTexture;

	public DepthFieldBlurRenderer(DepthFieldBlurShader shader, VAO quad) {
		super(shader, quad);
		this.shader = shader;
		shader.start();
		shader.connectTextureUnits();
		shader.nearFarPlanes.load(new Vector2f(0.1f, MasterRenderer.FAR_PLANE));
		shader.stop();
	}

	public void setBluredTexture(Texture bluredTexture) {
		this.bluredTexture = bluredTexture;
	}

	@Override
	public void apply(FBO source, FBO destination) throws FBOAttachmentException {
		if (destination != null) {
			destination.bind();
		} else
			FBO.unbind();
		MasterRenderer.prepare();
		if ((source.getConfig() & 3) != 3)
			throw new FBOAttachmentException(
					"The source got wrong configuration:" + source + " - configuration:" + source.getConfig());

		shader.start();
		
		source.getTex().bindToTextureUnit(0);
		source.getDepthTex().bindToTextureUnit(1);
		bluredTexture.bindToTextureUnit(2);
		
		quad.bind();
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		quad.unbind();

		shader.stop();

		if (destination != null)
			FBO.unbind();
	}
}