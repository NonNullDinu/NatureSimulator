/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
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

import ns.camera.ICamera;
import ns.openglObjects.VAO;
import ns.rivers.River;
import ns.shaders.RiverShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class RiverRenderer {
	private final RiverShader shader;

	public RiverRenderer(Matrix4f projectionMatrix) {
		shader = new RiverShader();
		shader.start();
		shader.projectionMatrix.load(projectionMatrix);
		shader.stop();
	}

	public void render(List<River> rivers, ICamera camera) {
		shader.start();
		shader.viewMatrix.load(camera.getViewMatrix());
		shader.color.load(new Vector3f(0.6f, 0.9f, 0.9f));
		shader.skyColor.load(MasterRenderer.CLEAR_COLOR);
		shader.fogValues.load(MasterRenderer.FOG_VALUES);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int vaolen;
		for (River river : rivers) {
			if ((vaolen = river.vao_length()) != 0) {
				VAO model = river.getModel();
				model.bind(0, 1, 2);
				shader.loc.load(0);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, vaolen);
				shader.loc.load(1);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, vaolen);
				shader.loc.load(2);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, vaolen);
				model.unbind();
			}
		}
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}