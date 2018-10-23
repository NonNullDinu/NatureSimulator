package ns.parallelComputing;

import data.GameData;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

public class Thread extends java.lang.Thread {

	public final List<Request> toCarryOutRequests = new ArrayList<>();
	public final List<CreateVAORequest> vaoCreateRequests = new ArrayList<>();
	private final Runnable runnable;
	public final List<Request> renderingRequests = new ArrayList<>();
	private long timeb;
	public boolean isExecutingRequests;

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
		while (!Display.isCreated())
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