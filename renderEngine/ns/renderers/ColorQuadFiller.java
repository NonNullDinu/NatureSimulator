package ns.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.openglObjects.VAO;
import ns.shaders.ColorQuadFillShader;
import ns.utils.Maths;

public class ColorQuadFiller {
	private static ColorQuadFillShader shader;
	private static VAO quad;
	
	public static void init() {
		shader = new ColorQuadFillShader();
		quad = MasterRenderer.standardModels.get(0);
	}
	
	public static void render(Vector2f center, Vector2f scale, Vector3f color) {
		shader.start();
		quad.bind();
		shader.transformationMatrix.load(Maths.createTransformationMatrix(center, scale));
		shader.color.load(color);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		quad.unbind();
		shader.stop();
	}
	
	public static void cleanUp() {
		shader.cleanUp();
	}
}