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

public class MovingEntitySpotShader extends ShaderProgram {
	private static final String VERTEX_SHADER = "movingEntity/movingEntitySpotVertex.glsl";
	private static final String FRAGMENT_SHADER = "movingEntity/movingEntitySpotFragment.glsl";
	public UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");
	public UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public UniformFloat[] y = locator.getArrayLocation("y");
//	private UniformFloat[] y = new UniformFloat[] {
//			new UniformFloat("y[0]"),
//			new UniformFloat("y[1]"),
//			new UniformFloat("y[2]"),
//			new UniformFloat("y[3]"),
//			new UniformFloat("y[4]"),
//			new UniformFloat("y[5]"),
//			new UniformFloat("y[6]"),
//			new UniformFloat("y[7]"),
//			new UniformFloat("y[8]"),
//	};

	public MovingEntitySpotShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
	}
}