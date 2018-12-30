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

import ns.exceptions.FBOAttachmentException;
import ns.openglObjects.FBO;
import ns.openglObjects.VAO;
import ns.shaders.ShaderProgram;

abstract class EffectRenderer {
	final VAO quad;
	private final ShaderProgram shader;

	public EffectRenderer(ShaderProgram shader, VAO quad) {
		this.shader = shader;
		this.quad = quad;
	}

	void cleanUp() {
		shader.cleanUp();
	}

	public abstract void apply(FBO source, FBO destination) throws FBOAttachmentException;
}