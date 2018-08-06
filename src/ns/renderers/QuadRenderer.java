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
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.color.load(color);
		shader.config.load(shader.COLOR_FILL);
		shader.multFactor.load(1);
		shader.z.load(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind();
		shader.stop();
	}

	public static void render(Vector2f center, Vector2f scale, Texture tex) {
		render(center, scale, tex, false);
	}
	
	public static void render(Vector2f center, Vector2f scale, Texture tex, boolean alphaDiscard) {
		if (center == null)
			return;
		shader.start();
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load((alphaDiscard ? shader.TEXTURE_FILL_ALPHA_01 : shader.TEXTURE_FILL));
		shader.multFactor.load(1);
		shader.z.load(0);
		tex.bindToTextureUnit(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind();
		shader.stop();
	}
	
	public static void render(Vector2f center, Vector2f scale, Texture tex, boolean alphaDiscard, int blend) {
		if (center == null)
			return;
		shader.start();
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, blend);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load((alphaDiscard ? shader.TEXTURE_FILL_ALPHA_01 : shader.TEXTURE_FILL));
		shader.multFactor.load(1);
		shader.z.load(0);
		tex.bindToTextureUnit(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		quad.unbind();
		shader.stop();
	}
	
	public static void renderMaxDepth(Vector2f center, Vector2f scale, Texture tex, boolean alphaDiscard, int blend) {
		if (center == null)
			return;
		shader.start();
		quad.bind(0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, blend);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load((alphaDiscard ? shader.TEXTURE_FILL_ALPHA_01 : shader.TEXTURE_FILL));
		shader.multFactor.load(1);
		shader.z.load(0.999f);
		tex.bindToTextureUnit(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glDisable(GL11.GL_BLEND);
		quad.unbind();
		shader.stop();
	}

	public static void render(Vector2f center, Vector2f scale, Texture tex, int blend) {
		if (center == null)
			return;
		shader.start();
		quad.bind(0);
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
		render(center, scale, tex, blend, multFactor, false);
	}

	public static void render(Vector2f center, Vector2f scale, Texture tex, int blend, float multFactor,
			boolean alphaDiscard) {
		if (center == null)
			return;
		shader.start();
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load((alphaDiscard ? shader.TEXTURE_FILL_ALPHA_01 : shader.TEXTURE_FILL));
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

	public static void render(Vector2f center, Vector2f scale, Texture tex, int blend, float multFactor,
			boolean alphaDiscard, float rot) {
		if (center == null)
			return;
		shader.start();
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale, rot));
		shader.config.load((alphaDiscard ? shader.TEXTURE_FILL_ALPHA_01 : shader.TEXTURE_FILL));
		shader.multFactor.load(multFactor);
		shader.z.load(0);
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
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.color.load(transparencyColor);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load(shader.TEXTURE_FILL_TRANSPARENCY_COLOR);
		shader.multFactor.load(1);
		shader.z.load(0);
		tex.bindToTextureUnit(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind();
		shader.stop();
	}

	public static void cleanUp() {
		shader.cleanUp();
	}

	public static void renderMaxDepth(Vector2f center, Vector2f scale, Vector3f color,
			int blend) {
		if (center == null)
			return;
		shader.start();
		quad.bind(0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, blend);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load(shader.COLOR_FILL);
		shader.multFactor.load(1);
		shader.color.load(color);
		shader.z.load(0.9999f);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL11.glDisable(GL11.GL_BLEND);
		quad.unbind();
		shader.stop();
	}

	public static void renderMaxDepth(Vector2f center, Vector2f scale, Texture tex, boolean b) {
		if (center == null)
			return;
		shader.start();
		quad.bind(0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.config.load((b ? shader.TEXTURE_FILL_ALPHA_01 : shader.TEXTURE_FILL));
		shader.multFactor.load(1);
		tex.bindToTextureUnit(0);
		shader.z.load(0.9999f);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		quad.unbind();
		shader.stop();
	}
}