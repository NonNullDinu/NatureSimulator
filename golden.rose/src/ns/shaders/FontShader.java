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

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "font/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "font/fontFragment.glsl";

	public final UniformVec3 color = new UniformVec3("color");
	public final UniformVec2 translation = new UniformVec2("translation");
	public final UniformFloat alphaCoef = new UniformFloat("alpha");

	public FontShader() {
		super(new Shader(VERTEX_FILE, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_FILE, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(color, translation, alphaCoef);
	}
}