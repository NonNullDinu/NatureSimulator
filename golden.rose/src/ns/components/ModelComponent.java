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

package ns.components;

import ns.openglObjects.VAO;

public class ModelComponent implements IComponent {
	private static final long serialVersionUID = 584478380828112581L;

	public boolean heightStop = false;
	public float stopMovementHeight;
	private transient VAO model;
	private boolean shouldScale = false;

	ModelComponent(VAO model) {
		this.model = model;
	}

	public VAO getModel() {
		return model;
	}

	public void setModel(VAO model) {
		this.model = model;
	}

	ModelComponent shouldScaleTrue() {
		this.shouldScale = true;
		return this;
	}

	ModelComponent useHeightStopMovement(float heightStop) {
		this.stopMovementHeight = heightStop;
		this.heightStop = true;
		return this;
	}

	public boolean shouldScale() {
		return shouldScale;
	}

	@Override
	public IComponent copy() {
		return heightStop ? new ModelComponent(model).useHeightStopMovement(stopMovementHeight) :
				new ModelComponent(model);
	}
}