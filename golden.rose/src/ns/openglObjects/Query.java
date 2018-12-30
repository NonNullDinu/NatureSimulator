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

package ns.openglObjects;

import ns.parallelComputing.CreateQueryRequest;
import ns.utils.GU;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class Query implements IOpenGLObject {
	private final int type;
	private int id;
	private boolean created;
	private boolean isInUse;
	private boolean beginCalled;

	public Query(int type) {
		this.type = type;
		create();
	}

	public int getResult() {
		isInUse = false;
		return GL15.glGetQueryObjecti(id, GL15.GL_QUERY_RESULT);
	}

	public boolean isResultAvailable() {
		return GL15.glGetQueryObjecti(id, GL15.GL_QUERY_RESULT_AVAILABLE) == GL11.GL_TRUE;
	}

	public void beginQuery() {
		GL15.glBeginQuery(type, id);
		isInUse = true;
		beginCalled = true;
	}

	public void endQuery() {
		GL15.glEndQuery(type);
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Query create() {
		if (GU.currentThread().getName().equals(GU.MAIN_THREAD_NAME)) {
			this.id = GL15.glGenQueries();
			created = true;
		} else
			GU.sendRequestToMainThread(new CreateQueryRequest(this));
		return this;
	}

	public boolean isInUse() {
		return isInUse;
	}

	@Override
	public void delete() {
		GL15.glDeleteQueries(id);
		created = false;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public boolean isCreated() {
		return created;
	}

	public boolean wasBeginCalled() {
		return beginCalled;
	}
}