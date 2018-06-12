package ns.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import ns.shaders.GUIShader;
import ns.ui.GUIButton;
import ns.ui.GUITexture;
import ns.utils.Maths;

public class GUIRenderer {
	public static final Vector3f TRANSPARENCY = new Vector3f(1.0f, 0.016f, 0.839f);
	public static GUIRenderer instance;
	private GUIShader shader;
	private VAO quad;
	
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
	
	protected void batchRenderCall(GUITexture texture) {
		texture.getTexture().bindToTextureUnit(0);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(texture.getCenter(), texture.getScale()));
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	protected void batchRenderCall(Vector2f center, Vector2f scale, Texture tex) {
		tex.bindToTextureUnit(0);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	protected void bind() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
		quad.bind();
	}
	
	protected void unbind() {
		quad.unbind();
		shader.stop();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void render(List<GUITexture> guis) {
		bind();
		for(GUITexture gui : guis)
			batchRenderCall(gui);
		unbind();
	}

	public void render(GUIButton button) {
		bind();
		batchRenderCall(button.getCenter(), button.getScale(), button.getTexture());
		unbind();
	}
}