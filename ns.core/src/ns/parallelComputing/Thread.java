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

import data.GameData;
import ns.display.DisplayManager;

import java.util.ArrayList;
import java.util.List;

public class Thread extends java.lang.Thread {

	public final List<Request> toCarryOutRequests = new ArrayList<>();
	public final List<CreateVAORequest> vaoCreateRequests = new ArrayList<>();
	public final List<Request> renderingRequests = new ArrayList<>();
	private final Runnable runnable;
	public boolean isExecutingRequests;
	private long timeb;

	protected Thread(String name, Runnable runnable) {
		super(runnable, name);
		this.runnable = runnable;
	}

	public synchronized void setToCarryOutRequest(Request request) {
		while (isExecutingRequests)
			Thread.yield();
		if (request instanceof CreateVAORequest)
			vaoCreateRequests.add((CreateVAORequest) request);
		else if (request instanceof GLClearRequest || request instanceof GLRenderRequest
				|| request instanceof UpdateDisplayRequest) {
			renderingRequests.add(request);
		} else
			this.toCarryOutRequests.add(request);
	}

	public synchronized void clearRequests() {
		toCarryOutRequests.clear();
		vaoCreateRequests.clear();
		renderingRequests.clear();
	}

	public Runnable getRunnable() {
		return runnable;
	}

	@Override
	public void start() {
		super.start();
		this.timeb = System.nanoTime();
	}

	public void waitForDisplayInit() {
        while (DisplayManager.window == 0)
			Thread.yield();
	}

	public void waitForGameDataInit() {
		while (!GameData.initialized())
			java.lang.Thread.yield();
	}

	public void finishLoading() {
		System.out.println(getName() + " finished loading in " + (System.nanoTime() - timeb));
	}

	public void finishExecution() {
		System.out.println(getName() + " finished execution");
	}
}