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

package ns.worldSave.NSSV1100;

import ns.rivers.River;
import ns.worldSave.Data;
import org.lwjgl.util.vector.Vector3f;

public class RiverData extends Data {
	private static final long serialVersionUID = -2850921767663909329L;

	private Vector3f source;

	public RiverData withSource(Vector3f source) {
		this.source = source;
		return this;
	}

	@Override
	public River asInstance() {
		return new River(source);
	}
}