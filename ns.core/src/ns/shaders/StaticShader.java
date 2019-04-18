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
import ns.shaders.uniformStructs.UniformLight;
import org.lwjgl.opengl.GL20;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "standard/vertexShader.glsl";
	private static final String FRAGMENT_SHADER = "standard/fragmentShader.glsl";

	public final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	public final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");

	public final UniformLight sun = new UniformLight("sun", locator);
	public final UniformLight moon = new UniformLight("moon", locator);

	public final UniformVec4 clipPlane = new UniformVec4("clipPlane");
	//	public UniformVec3[] customColors = { new UniformVec3("customColors[0]"), new UniformVec3("customColors[1]"),
//			new UniformVec3("customColors[2]") };
	public final UniformVec3[] customColors = locator.getArrayLocation("customColors");

	public final UniformVec3 skyColor = new UniformVec3("skyColor");

	public final UniformFogValues fogValues = new UniformFogValues("fogValues", locator);

	public final UniformFloat time = new UniformFloat("time");
	public final UniformFloat alpha = new UniformFloat("alpha");

	public final UniformFloat movementStopHeight = new UniformFloat("stopMovementHeight");

	public StaticShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(projectionMatrix, viewMatrix, transformationMatrix, sun, moon, clipPlane, customColors[0],
				customColors[1], customColors[2], skyColor, fogValues, time, alpha, movementStopHeight);
	}
}
