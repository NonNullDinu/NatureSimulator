/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.renderers;

import ns.exceptions.FBOAttachmentException;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import ns.shaders.DepthFieldBlurShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class DepthFieldBlurRenderer extends EffectRenderer {
	private final DepthFieldBlurShader shader;
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

		quad.bind(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		quad.unbind();

		shader.stop();

		if (destination != null)
			FBO.unbind();
	}
}