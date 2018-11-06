package ns.renderers;

import ns.exceptions.FBOAttachmentException;
import ns.openglObjects.FBO;
import ns.openglObjects.VAO;
import ns.shaders.HBlurShader;
import ns.shaders.VBlurShader;
import org.lwjgl.opengl.GL11;

public class Blurer extends EffectRenderer {

	private final FBO helper;
	private final VBlurer vblurer;
	private final HBlurer hblurer;

	public Blurer(VAO quad) {
		super(null, quad);
		helper = new FBO(1200, 800, (FBO.COLOR_TEXTURE)).create();
		vblurer = new VBlurer(new VBlurShader(), quad);
		hblurer = new HBlurer(new HBlurShader(), quad);
	}

	@Override
	public void apply(FBO source, FBO destination) throws FBOAttachmentException {
		if ((source.getConfig() & 1) != 1)
			throw new FBOAttachmentException(source);
		hblurer.apply(source, helper);
		vblurer.apply(helper, destination);
	}

	private class VBlurer extends EffectRenderer {
		private final VBlurShader shader;

		VBlurer(VBlurShader shader, VAO quad) {
			super(shader, quad);
			this.shader = shader;
		}

		@Override
		public void apply(FBO source, FBO destination) throws FBOAttachmentException {
			if (destination != null) {
				destination.bind();
			} else {
				FBO.unbind();
			}
			MasterRenderer.prepare();

			shader.start();
			shader.size.load(source.getSize());

			quad.bind(0);

			source.getTex().bindToTextureUnit(0);

			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

			quad.unbind();

			shader.stop();

			if (destination != null)
				FBO.unbind();
		}
	}

	private class HBlurer extends EffectRenderer {
		private final HBlurShader shader;

		HBlurer(HBlurShader shader, VAO quad) {
			super(shader, quad);
			this.shader = shader;
		}

		@Override
		public void apply(FBO source, FBO destination) throws FBOAttachmentException {
			destination.bind();
			MasterRenderer.prepare();

			shader.start();
			shader.size.load(source.getSize());

			quad.bind(0);

			source.getTex().bindToTextureUnit(0);

			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			quad.unbind();

			shader.stop();

			FBO.unbind();
		}
	}
}