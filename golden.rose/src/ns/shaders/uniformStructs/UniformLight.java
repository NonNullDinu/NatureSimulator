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

package ns.shaders.uniformStructs;

import ns.entities.Light;
import ns.shaders.UniformLocator;
import ns.shaders.UniformVar;
import ns.shaders.UniformVec2;
import ns.shaders.UniformVec3;

import java.util.ArrayList;
import java.util.List;

public class UniformLight extends UniformStruct {

	public UniformLight(String name, UniformLocator locator) {
		super(name, getAttributes(name, locator));
	}

	private static List<UniformVar> getAttributes(String name, UniformLocator locator) {
		List<UniformVar> attributes = new ArrayList<>();
		attributes.add(new UniformVec3(name + ".direction"));
		attributes.add(new UniformVec3(name + ".color"));
		attributes.add(new UniformVec2(name + ".bias"));
		return attributes;
	}

	public void load(Light light) {
		while (light == null)
			Thread.yield();
		super.load(new UniformValue(
				light.dir), new UniformValue(light.color), new UniformValue(light.bias));
	}
}