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

package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;

public class VBORecreateAndReplaceRequest extends Request {

	private final VAO model;
	private final int attn;
	private final float[] dataf;
	private final byte[] datab;
	private final int usage;

	public VBORecreateAndReplaceRequest(VAO model, int attn, float[] data, int usage) {
		this.model = model;
		this.attn = attn;
		this.dataf = data;
		this.datab = null;
		this.usage = usage;
	}

	public VBORecreateAndReplaceRequest(VAO model, int attn, byte[] data, int usage) {
		this.model = model;
		this.attn = attn;
		this.datab = data;
		this.dataf = null;
		this.usage = usage;
	}

	@Override
	public void execute() {
		if (dataf == null)
			VAOLoader.recreateAndReplace(model, attn, datab, usage);
		else
			VAOLoader.recreateAndReplace(model, attn, dataf, usage);
	}
}