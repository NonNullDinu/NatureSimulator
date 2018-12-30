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

import ns.shaders.UniformLocator;
import ns.shaders.UniformVar;

import java.util.List;

public abstract class UniformStruct extends UniformVar {

	private final String name;
	private final List<UniformVar> attributes;

	UniformStruct(String name, List<UniformVar> attributes) {
		super(-1, name);
		this.name = name;
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	protected void load(List<UniformValue> values) {
		int itn = Math.min(attributes.size(), values.size());
		for (int i = 0; i < itn; i++) {
			values.get(i).loadTo(attributes.get(i));
		}
	}

	void load(UniformValue... values) {
		int itn = Math.min(attributes.size(), values.length);
		for (int i = 0; i < itn; i++) {
			values[i].loadTo(attributes.get(i));
		}
	}

	@Override
	public void loadLocation(UniformLocator locator) {
		for (UniformVar attribute : attributes)
			attribute.loadLocation(locator);
	}
}