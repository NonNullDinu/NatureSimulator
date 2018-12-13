package ns.renderers;

import ns.fontMeshCreator.FontType;
import ns.fontMeshCreator.GUIText;
import ns.shaders.FontShader;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public class FontRenderer {

	private final FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}

	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		shader.alphaCoef.load(1f);
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
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	private void renderText(GUIText text) {
		text.getMesh().bind(0, 1);
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

	public void render(Map<FontType, List<GUIText>> texts, float alphaCoef) {
		prepare();
		shader.alphaCoef.load(alphaCoef);
		for (FontType font : texts.keySet()) {
			font.getTextureAtlas().bindToTextureUnit(0);
			for (GUIText text : texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}
}