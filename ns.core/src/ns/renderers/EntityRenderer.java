package ns.renderers;

import ns.components.Blueprint;
import ns.components.CustomColorsComponent;
import ns.components.ModelComponent;
import ns.entities.Entity;
import ns.entities.Light;
import ns.openglObjects.VAO;
import ns.shaders.StaticShader;
import ns.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;
import java.util.Map;

class EntityRenderer {

	private final StaticShader shader;

	EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.projectionMatrix.load(projectionMatrix);
		shader.stop();
	}

	public void render(Map<VAO, List<Entity>> entities) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		for (VAO vao : entities.keySet()) {
			vao.bind(0, 1, 2, 3, 4, 5);
			for (Entity e : entities.get(vao)) {
				shader.transformationMatrix.load(Maths.createTreansformationMatrix(e));
				if (e.getLifeComponent() != null && e.getLifeComponent().isDead())
					shader.alpha.load(e.getAlpha());
				else shader.alpha.load(e.getAlpha());
				CustomColorsComponent customColors = e.getCustomColors();
				if (customColors != null)
					for (int i = 0; i < customColors.getColors().size(); i++) {
						shader.customColors[i].load(customColors.getColors().get(i));
					}
				ModelComponent mc = e.getModelComponent();
				shader.movementStopHeight.load(mc.heightStop ? mc.stopMovementHeight : -100f);
				vao.batchRenderCall();
			}
			vao.unbind();
		}
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void render(Blueprint blueprint, Vector3f position) {
		shader.start();
		shader.viewMatrix.load(new Matrix4f());
		shader.transformationMatrix.load(Maths.createTransformationMatrix(position, 0, 0, 0, 1));
		shader.clipPlane.load(new Vector4f(0, 0, 0, 0));
		shader.sun.load(new Light(new Vector3f(0.5f, 0, -0.5f), new Vector3f(1, 1, 1), new Vector2f(0.5f, 0.5f)));
		shader.moon.load(new Light(new Vector3f(0.5f, 0, -0.5f), new Vector3f(1, 1, 1), new Vector2f(0, 0)));
		CustomColorsComponent customColors = blueprint.getCustomColors();
		if (customColors != null)
			for (int i = 0; i < customColors.getColors().size(); i++) {
				shader.customColors[i].load(customColors.getColors().get(i));
			}
		shader.movementStopHeight.load(-100f);
		VAO vao = blueprint.getModel().getModel();
		vao.bind(0, 1, 2, 3, 4, 5);
		vao.batchRenderCall();
		vao.unbind();
		shader.stop();
	}
}
