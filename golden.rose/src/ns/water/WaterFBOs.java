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

package ns.water;

import ns.openglObjects.FBO;

public class WaterFBOs {
	private static final int REFLECTION_WIDTH = 2048, REFRACTION_WIDTH = 1440,
			REFLECTION_HEIGHT = 2048,
			REFRACTION_HEIGHT = 720;

	private final FBO reflection;
	private final FBO refraction;
//	private FBO bluredReflection;
//	private FBO bluredRefraction;

	public WaterFBOs(boolean blured) {
		reflection = new FBO(REFLECTION_WIDTH, REFLECTION_HEIGHT, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		refraction = new FBO(REFRACTION_WIDTH, REFRACTION_HEIGHT, (FBO.COLOR_TEXTURE | FBO.DEPTH_TEXTURE)).create();
//		bluredReflection = new FBO(REFLECTION_WIDTH, REFLECTION_HEIGHT, (FBO.COLOR_TEXTURE)).create();
//		bluredRefraction = new FBO(REFRACTION_WIDTH, REFRACTION_HEIGHT, (FBO.COLOR_TEXTURE)).create();
	}

	public FBO getReflection() {
		return reflection;
	}

	public FBO getRefraction() {
		return refraction;
	}

	public void bindReflection() {
		reflection.bind();
	}

	public void bindRefraction() {
		refraction.bind();
	}

//	public void blur(Blurer blurer) {
//		blurer.apply(reflection, bluredReflection);
//		blurer.apply(refraction, bluredRefraction);
//	}
//
//	public FBO getBluredReflection() {
//		return bluredReflection;
//	}
//
//	public FBO getBluredRefraction() {
//		return bluredRefraction;
//	}
}