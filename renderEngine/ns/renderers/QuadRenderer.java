package ns.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import ns.shaders.QuadShader;
import ns.utils.Maths;

public class QuadRenderer {
	private static QuadShader shader;
	private static VAO quad;

	public static void init() {
		shader = new QuadShader();
		quad = MasterRenderer.standardModels.get(0);
	}

	public static void render(Vector2f center, Vector2f scale, Vector3f color) {
		if (center == null)
			return;
		shader.start();
		quad.bind();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.color.load(color);
		shader.config.load(shader.COLOR_FILL);
		shader.multFactor.load(1);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind();
		shader.stop();
	}

	public static void render(Vector2f center, Vector2f scale, Texture tex) {
		if (center == null)
			return;
		shader.start();
		quad.bind();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load(shader.TEXTURE_FILL);
		shader.multFactor.load(1);
		tex.bindToTextureUnit(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind();
		shader.stop();
	}

	public static void render(Vector2f center, Vector2f scale, Texture tex, int blend) {
		if (center == null)
			return;
		shader.start();
		quad.bind();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load(shader.TEXTURE_FILL);
		shader.multFactor.load(1);
		tex.bindToTextureUnit(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, blend);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind();
		shader.stop();
	}

	public static void render(Vector2f center, Vector2f scale, Texture tex, int blend, float multFactor) {
		if (center == null)
			return;
		shader.start();
		quad.bind();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load(shader.TEXTURE_FILL);
		shader.multFactor.load(multFactor);
		tex.bindToTextureUnit(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, blend);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind();
		shader.stop();
	}

	public static void render(Vector2f center, Vector2f scale, Texture tex, Vector3f transparencyColor) {
		if (center == null)
			return;
		shader.start();
		quad.bind();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.color.load(transparencyColor);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load(shader.TEXTURE_FILL_TRANSPARENCY_COLOR);
		shader.multFactor.load(1);
		tex.bindToTextureUnit(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind();
		shader.stop();
	}

	public static void cleanUp() {
		shader.cleanUp();
	}
}