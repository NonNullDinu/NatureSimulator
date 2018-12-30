/*
 * Copyright (C) 2018  Dinu Blanovschi
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
				shader.transformationMatrix.load(Maths.createTransformationMatrix(e));
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
		ModelComponent mc = blueprint.getModel();
		shader.movementStopHeight.load(mc.heightStop ? mc.stopMovementHeight : -100f);
		VAO vao = blueprint.getModel().getModel();
		vao.bind(0, 1, 2, 3, 4, 5);
		vao.batchRenderCall();
		vao.unbind();
		shader.stop();
	}

	public void render(Blueprint blueprint, Vector3f position, float rotX, float rotY, float rotZ, float scale, Vector3f color) {
		shader.start();
		shader.viewMatrix.load(new Matrix4f());
		shader.transformationMatrix.load(Maths.createTransformationMatrix(position, rotX, rotY, rotZ, scale));
		shader.clipPlane.load(new Vector4f(0, 0, 0, 0));
		shader.sun.load(new Light(new Vector3f(0.5f, 0, -0.5f), color, new Vector2f(0.5f, 0.5f)));
		shader.moon.load(new Light(new Vector3f(0.5f, 0, -0.5f), color, new Vector2f(0, 0)));
		shader.movementStopHeight.load(-100f);
		VAO model = blueprint.getModel().getModel();
		model.bindAll();
		model.batchRenderCall();
		model.unbind();
		shader.stop();
	}
}
