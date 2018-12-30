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

import ns.camera.ICamera;
import ns.entities.Entity;
import ns.mainMenu.MainMenu;
import ns.mainMenu.MainMenuButton;
import ns.openglObjects.FBO;
import ns.shaders.MenuDNAShader;
import ns.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class MainMenuRenderer {
	private static MainMenuRenderer renderer;
	private final GUIRenderer guiRenderer;
	private final FBO DNAFBO;
	private final MenuDNAShader shader;

	public MainMenuRenderer(GUIRenderer guiRenderer, ICamera camera) {
		this.guiRenderer = guiRenderer;
		DNAFBO = new FBO(600, 800, FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER).create();
		shader = new MenuDNAShader();
		shader.start();
		shader.projectionMatrix.load(camera.getProjectionMatrix());
		shader.stop();
		renderer = this;
	}

	public static MenuDNAShader getShader() {
		return renderer.shader;
	}

	public void render(MainMenu menu) {
		DNAFBO.bind();
		GL11.glClearColor(GUIRenderer.TRANSPARENCY.x, GUIRenderer.TRANSPARENCY.y, GUIRenderer.TRANSPARENCY.z, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		shader.start();
		Entity DNA = menu.getDNA();
		DNA.getModel().bind(0);
		shader.transformationMatrix.load(Maths.createTransformationMatrix(DNA));
		DNA.getModel().batchRenderCall();
		DNA.getModel().unbind();
		shader.stop();
		FBO.unbind();
		guiRenderer.bind();
		Vector4f dnaLoc = menu.getDnaLocation();
		guiRenderer.batchRenderCall(new Vector2f(dnaLoc.x, dnaLoc.y), new Vector2f(dnaLoc.z, dnaLoc.w), DNAFBO.getTex());
		for (MainMenuButton button : menu.getButtons())
			guiRenderer.batchRenderCall(button.getCenter(), button.getScale(), button.getTex());
		guiRenderer.unbind();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}