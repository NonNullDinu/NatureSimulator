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

package ns.ui;

import ns.openglObjects.IRenderable;
import ns.renderers.GUIRenderer;
import ns.renderers.QuadRenderer;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class ComplexGUI implements IRenderable/*,GUI*/ {
	private final Vector2f center;
	private final Vector2f scale;
	private final List<GUITexture> others;
	private final GUIRenderer guiRenderer;

	public ComplexGUI(Vector2f center, Vector2f scale, List<GUITexture> others, GUIRenderer guiRenderer) {
		this.center = center;
		this.scale = scale;
		this.others = others;
		this.guiRenderer = guiRenderer;
	}

	public Vector2f getCenter() {
		return center;
	}

	public Vector2f getScale() {
		return scale;
	}

	public List<GUITexture> getOthers() {
		return others;
	}

	@Override
	public void render() {
		QuadRenderer.render(center, scale, new Vector3f(0.5f, 0.5f, 0.5f));
		guiRenderer.render(others);
	}

//	@Override
//	public void render(float[] args, int... argType) {
//		QuadRenderer.render(center, scale, new Vector3f(0.5f, 0.5f, 0.5f));
//		guiRenderer.render(others);
//	}

	@Override
	public void batchRenderCall() {
		render();
	}
}