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

package ns.ui.loadingScreen;

import ns.entities.Entity;
import ns.openglObjects.FBO;
import ns.renderers.GUIRenderer;
import ns.renderers.MainMenuRenderer;
import ns.renderers.MasterRenderer;
import ns.shaders.MenuDNAShader;
import ns.ui.GUI;
import ns.utils.GU;
import ns.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class InGameLogo implements GUI {
	private Entity entity;
	private float rot = 260;
	private FBO fbo;

	public InGameLogo(Entity entity) {
		this.entity = entity;
		this.fbo = new FBO(512, 512, FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER).create();
	}

	@Override
	public void render(GUIRenderer renderer, float[] args, int... argType) {
		MenuDNAShader sh = MainMenuRenderer.getShader();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		fbo.bind();
		sh.start();
		GL11.glClearColor(GUIRenderer.TRANSPARENCY.x, GUIRenderer.TRANSPARENCY.y, GUIRenderer.TRANSPARENCY.z, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		MasterRenderer.disableBackfaceCulling();
		sh.transformationMatrix.load(Maths.createTransformationMatrix(entity.getPosition(), 0, rot, 0, entity.getScale()));
		entity.getModel().bind(0);
		entity.getModel().batchRenderCall();
		entity.getModel().unbind();
		rot -= (GU.smoothstep(180, 270, rot + 10) - GU.smoothstep(180, 270, rot)) * 5f;
		sh.stop();
		FBO.unbind();
		renderer.bind();
		renderer.batchRenderCall(new Vector2f(), new Vector2f(0.3f, 0.3f), fbo.getTex());
		renderer.unbind();
	}

	public float getRot() {
		return rot;
	}

	public boolean done() {
		return rot <= 180;
	}
}
