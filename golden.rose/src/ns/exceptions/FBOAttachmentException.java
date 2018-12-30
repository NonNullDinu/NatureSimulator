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

package ns.exceptions;

import ns.openglObjects.FBO;

/**
 * Thrown when attempting to render an effect from a FBO that doesn't have the right configuration
 *
 * @author Dinu B.
 */
public class FBOAttachmentException extends RenderException {
	private static final long serialVersionUID = -4759771195837122461L;

	public FBOAttachmentException(String message) {
		super(message);
	}

	public FBOAttachmentException(FBO fbo) {
		this("The source got wrong configuration:" + fbo + " - configuration:" + fbo.getConfig());
	}
}