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

import ns.shaders.uniformStructs.UniformFogValues;
import org.lwjgl.opengl.GL20;

public class RiverShader extends ShaderProgram {
	public final UniformVec3 color = new UniformVec3("color");
	public final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public final UniformInt loc = new UniformInt("loc");
	public final UniformVec3 skyColor = new UniformVec3("skyColor");
	public final UniformFogValues fogValues = new UniformFogValues("fogValues", locator);
	private final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");

	public RiverShader() {
		super(new Shader("river/riverVertex.glsl", GL20.GL_VERTEX_SHADER),
				new Shader("river/riverFragment.glsl", GL20.GL_FRAGMENT_SHADER));
		storeUniforms(transformationMatrix, color, projectionMatrix, viewMatrix, loc, skyColor, fogValues);
	}
}