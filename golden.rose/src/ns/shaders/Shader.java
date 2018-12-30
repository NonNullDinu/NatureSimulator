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

import ns.openglObjects.IOpenGLObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader implements IOpenGLObject {
	final int type;
	private final String name;
	private String src;
	private int ID;
	private boolean created = false;

	public Shader(String name, int type) {
		while (this.src == null)
			this.src = ShaderLib.getSource("gameData/shaders/" + name);
		this.name = name;
		this.type = type;
	}

	public void bindToProgram(ShaderProgram program) {
		GL20.glAttachShader(program.getID(), this.ID);
	}

	public String getSource() {
		return src;
	}

	@Override
	public Shader create() {
		created = true;
		ID = GL20.glCreateShader(type);
		GL20.glShaderSource(ID, src);
		GL20.glCompileShader(ID);
		if (GL20.glGetShaderi(ID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
			System.err.println("Could not compile shader " + name + ", log:" + GL20.glGetShaderInfoLog(ID, 200));
			System.exit(-1);
		}
		return this;
	}

	@Override
	public void delete() {
		GL20.glDeleteShader(ID);
		created = false;
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public boolean isCreated() {
		return created;
	}
}