package ns.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import ns.exceptions.FBOAttachmentException;
import ns.openglObjects.FBO;
import ns.openglObjects.VAO;
import ns.shaders.HBlurShader;
import ns.shaders.VBlurShader;

public class Blurer extends EffectRenderer {

	private FBO helper;
	private VBlurer vblurer;
	private HBlurer hblurer;

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

	@Override
	public void cleanUp() {
		helper.cleanUp();
		vblurer.cleanUp();
		hblurer.cleanUp();
	}

	private class VBlurer extends EffectRenderer {
		private VBlurShader shader;

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

			GL30.glBindVertexArray(quad.getId());
			GL20.glEnableVertexAttribArray(0);

			source.getTex().bindToTextureUnit(0);

			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);

			shader.stop();

			if (destination != null)
				FBO.unbind();
		}
	}

	private class HBlurer extends EffectRenderer {
		private HBlurShader shader;

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

			GL30.glBindVertexArray(quad.getId());
			GL20.glEnableVertexAttribArray(0);

			source.getTex().bindToTextureUnit(0);

			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);

			shader.stop();

			FBO.unbind();
		}
	}
}