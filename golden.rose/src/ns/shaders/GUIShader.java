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

package ns.shaders;

import org.lwjgl.opengl.GL20;

public class GUIShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "guis/guiVertex.glsl";
	private static final String FRAGMENT_SHADER = "guis/guiFragment.glsl";

	public final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");

	public final UniformFloat alpha = new UniformFloat("alpha");

	public GUIShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(transformationMatrix, alpha);
	}

	public void fillOtherArgs(float[] others) {
		if (others == null) {
			alpha.load(1);
			return;
		}
		float val = 0;
		for (int i = 0; i < 1; i++) {
			if (i >= others.length) {
				if (i == 0)
					val = 1;
			} else val = others[i];
			switch (i) {
				case 0:
					alpha.load(val);
					break;
			}
		}
	}
}