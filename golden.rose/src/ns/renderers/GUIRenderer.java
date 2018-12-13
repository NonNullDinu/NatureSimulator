package ns.renderers;

import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import ns.shaders.GUIShader;
import ns.ui.GUI;
import ns.ui.GUIButton;
import ns.ui.GUITexture;
import ns.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class GUIRenderer {
	public static final Vector3f TRANSPARENCY = new Vector3f(1.0f, 4f / 255f, 214f / 255f);
	public static GUIRenderer instance;
	private final GUIShader shader;
	private final VAO quad;

	public GUIRenderer(GUIShader shader, VAO quad) {
		instance = this;
		this.shader = shader;
		this.quad = quad;
	}

	public void render(GUITexture texture) {
		bind();
		batchRenderCall(texture);
		unbind();
	}

	private void batchRenderCall(GUITexture texture) {
		texture.getTexture().bindToTextureUnit(0);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(texture.getCenter(), texture.getScale()));
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	public void batchRenderCall(Vector2f center, Vector2f scale, Texture tex) {
		tex.bindToTextureUnit(0);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	public void bind() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
		shader.alpha.load(1);
		quad.bind(0);
	}

	public void unbind() {
		quad.unbind();
		shader.stop();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void render(List<GUITexture> guis) {
		bind();
		for (GUITexture gui : guis)
			batchRenderCall(gui);
		unbind();
	}

	public void render(GUIButton button) {
		bind();
		batchRenderCall(button.getCenter(), button.getScale(), button.getTexture());
		unbind();
	}

	public void render(GUI gui) {
		gui.render(this, null, null);
	}

	public void render(GUI gui, float[] args, int... argType) {
		gui.render(this, args, argType);
	}

	public void render(Vector2f position, Vector2f scale, Texture tex, float... others) {
		bind();
		batchRenderCall(position, scale, tex, others);
		unbind();
	}

	private void batchRenderCall(Vector2f center, Vector2f scale, Texture tex, float[] others) {
		tex.bindToTextureUnit(0);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.fillOtherArgs(others);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}
}