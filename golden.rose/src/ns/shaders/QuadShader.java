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

package ns.shaders;

import org.lwjgl.opengl.GL20;

public class QuadShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "quad/quadVertex.glsl";
	private static final String FRAGMENT_SHADER = "quad/quadFragment.glsl";

	public final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");
	public final UniformVec3 color = new UniformVec3("color");
	public final UniformInt config = new UniformInt("config");
	public final UniformFloat multFactor = new UniformFloat("multFactor");
	public final UniformFloat z = new UniformFloat("z");

	public final int COLOR_FILL = 1;
	public final int TEXTURE_FILL = 2;
	public final int TEXTURE_FILL_TRANSPARENCY_COLOR = 3;
	public final int TEXTURE_FILL_ALPHA_01 = 4;

	public QuadShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(transformationMatrix, color, config, multFactor, z);
	}
}