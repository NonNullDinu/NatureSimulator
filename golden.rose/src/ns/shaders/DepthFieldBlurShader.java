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

public class DepthFieldBlurShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "depthFieldBlur/vertex.glsl";
	private static final String FRAGMENT_SHADER = "depthFieldBlur/fragment.glsl";
	public final UniformVec2 nearFarPlanes = new UniformVec2("nearFarPlanes");
	private final UniformSampler2D colorTexture = new UniformSampler2D("colorTexture");
	private final UniformSampler2D depthTexture = new UniformSampler2D("depthTexture");
	private final UniformSampler2D bluredTexture = new UniformSampler2D("bluredTexture");

	public DepthFieldBlurShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(colorTexture, depthTexture, bluredTexture);
	}

	public void connectTextureUnits() {
		colorTexture.load(0);
		depthTexture.load(1);
		bluredTexture.load(2);
	}
}