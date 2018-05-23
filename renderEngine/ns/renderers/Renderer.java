package ns.renderers;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import ns.components.CustomColorsComponent;
import ns.entities.Entity;
import ns.openglObjects.VAO;
import ns.shaders.StaticShader;
import ns.utils.Maths;

public class Renderer {

	private StaticShader shader;

	public Renderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.projectionMatrix.load(projectionMatrix);
		shader.stop();
	}

	public void render(Map<VAO, List<Entity>> entities) {
		for (VAO vao : entities.keySet()) {
			vao.bind();
			for (Entity e : entities.get(vao)) {
				shader.transformationMatrix.load(Maths.createTreansformationMatrix(e));
				CustomColorsComponent customColors = e.getCustomColors();
				if(customColors != null)
					for(int i = 0; i < customColors.getColors().size(); i++) {
						shader.customColors[i].load(customColors.getColors().get(i));
					}
				vao.batchRenderCall();
			}
			vao.unbind();
		}
	}
}
