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

import java.util.Objects;

public final class UniformLocator {
	private final ShaderProgram program;

	public UniformLocator(ShaderProgram program) {
		this.program = program;
	}

	public int getLocation(String name) {
		return program.getLocation(name);
	}

	@SuppressWarnings("unchecked")
	public <T extends UniformVar> T[] getArrayLocation(String name) {
		String declLine = program.getDeclarationLineForArray(name);
		String[] pts = declLine.split(" ");
		String decl = pts[pts.length - 1];
		decl = decl.replace(name + "[", "").replace("];", "");
		int elemCnt = Integer.parseInt(decl);
		T[] vars = null;
		int ind = 0;
		switch (pts[pts.length - 2]) {
			case "float":
				vars = (T[]) new UniformFloat[elemCnt];
				ind = 0;
				break;
			case "vec2":
				vars = (T[]) new UniformVec2[elemCnt];
				ind = 1;
				break;
			case "vec3":
				vars = (T[]) new UniformVec3[elemCnt];
				ind = 2;
				break;
			case "vec4":
				vars = (T[]) new UniformVec4[elemCnt];
				ind = 3;
				break;
			case "mat4":
				vars = (T[]) new UniformMat4[elemCnt];
				ind = 4;
				break;
			case "int":
				vars = (T[]) new UniformInt[elemCnt];
				ind = 5;
				break;
			case "bool":
				vars = (T[]) new UniformBool[elemCnt];
				ind = 6;
				break;
			case "sampler2D":
				vars = (T[]) new UniformSampler2D[elemCnt];
				ind = 7;
				break;
		}
		for (int i = 0; i < elemCnt; i++) {
			switch (ind) {
				case 0:
					Objects.requireNonNull(vars)[i] = (T) new UniformFloat(name + "[" + i + "]");
					break;
				case 1:
					vars[i] = (T) new UniformVec2(name + "[" + i + "]");
					break;
				case 2:
					vars[i] = (T) new UniformVec3(name + "[" + i + "]");
					break;
				case 3:
					vars[i] = (T) new UniformVec4(name + "[" + i + "]");
					break;
				case 4:
					vars[i] = (T) new UniformMat4(name + "[" + i + "]");
					break;
				case 5:
					vars[i] = (T) new UniformInt(name + "[" + i + "]");
					break;
				case 6:
					vars[i] = (T) new UniformBool(name + "[" + i + "]");
					break;
				case 7:
					vars[i] = (T) new UniformSampler2D(name + "[" + i + "]");
					break;
			}
			vars[i].loadLocation(this);
		}
		return vars;
	}
}
