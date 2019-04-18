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

package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;

import java.util.List;

public class VBOUpdateRequest extends Request {

	private final VAO model;
	private final int attn;
	private final float[] data;
	private final List<Integer> changes;
	private final int dimensions;

	public VBOUpdateRequest(VAO model, int attn, float[] data, List<Integer> changes, int dimensions) {
		this.model = model;
		this.attn = attn;
		this.data = data;
		this.changes = changes;
		this.dimensions = dimensions;
	}

	@Override
	public void execute() {
		VAOLoader.replace(model, attn, data, changes, dimensions);
	}
}