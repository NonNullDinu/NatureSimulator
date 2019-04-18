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
import ns.terrain.Terrain;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.nio.ByteBuffer;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "terrain/terrainVertex.glsl";
	private static final String FRAGMENT_SHADER = "terrain/terrainFragment.glsl";

	public final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	public final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");
	public final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");

	public final UniformLight sun = new UniformLight("sun", locator);
	public final UniformLight moon = new UniformLight("moon", locator);

	public final UniformVec4 clipPlane = new UniformVec4("clipPlane");

	public final UniformVec3 skyColor = new UniformVec3("skyColor");

	public final UniformFogValues fogValues = new UniformFogValues("fogValues", locator);

	private int colors_buffer;

	public TerrainShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(projectionMatrix, transformationMatrix, viewMatrix, sun, moon, clipPlane, skyColor, fogValues);
	}

	@Override
	protected void postLink() {
		colors_buffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, colors_buffer);
		ByteBuffer buf = ByteBuffer.allocateDirect(Terrain.VERTEX_COUNT * Terrain.VERTEX_COUNT * 4);
		for (int i = 0; i < Terrain.VERTEX_COUNT * Terrain.VERTEX_COUNT; i++) {
			buf.put((byte) 0);
			buf.put((byte) 0);
			buf.put((byte) 0);
			buf.put((byte) 0);
		}
		buf.flip();
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, buf, GL15.GL_STATIC_DRAW);
		buf.clear();
		GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 0, colors_buffer);
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
	}

	@Override
	public void cleanUp() {
		GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 0, 0);
		GL15.glDeleteBuffers(colors_buffer);
		super.cleanUp();
	}
}
