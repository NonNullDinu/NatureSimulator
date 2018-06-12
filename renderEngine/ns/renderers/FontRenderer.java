package ns.renderers;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import ns.fontMeshCreator.FontType;
import ns.fontMeshCreator.GUIText;
import ns.shaders.FontShader;

public class FontRenderer {

	private FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}

	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font : texts.keySet()) {
			font.getTextureAtlas().bindToTextureUnit(0);
			for (GUIText text : texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepare() {
		shader.start();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private void renderText(GUIText text) {
		text.getMesh().bind();
		shader.color.load(text.getColour());
		shader.translation.load(text.getPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		text.getMesh().unbind();
	}

	private void endRendering() {
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shader.stop();
	}
}