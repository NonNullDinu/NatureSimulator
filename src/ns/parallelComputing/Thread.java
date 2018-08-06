package ns.parallelComputing;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import ns.interfaces.Condition;

public class Thread extends java.lang.Thread {
	public static enum ThreadState {
		WAITING, RUNNING, FINISHED
	}

	public List<Request> toCarryOutRequests = new ArrayList<>();
	public List<CreateVAORequest> vaoCreateRequests = new ArrayList<>();
	private Runnable runnable;
	public List<Request> renderingRequests = new ArrayList<>();
	private long timeb;
	public boolean isExecutingRequests;
	private ThreadState state;
	private boolean sple;

	protected Thread(String name, Runnable runnable) {
		super(runnable, name);
		this.runnable = runnable;
	}

	public synchronized void setToCarryOutRequest(Request request) {
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

	public void finishLoading() {
		System.out.println(getName() + " finished loading in " + (System.nanoTime() - timeb));
	}

	protected ThreadState state() {
		return state;
	}

	public void setState(ThreadState state) {
		this.state = state;
	}

	public void checkpoint() {
		ThreadMaster.checkpoint(this);
	}

	public void _stop() {
		state = ThreadState.WAITING;
		while (state != ThreadState.RUNNING)
			Thread.yield();
	}

	public void _stop(Condition c) {
		sple = true;
		state = ThreadState.WAITING;
		while (c.value()) {
			Thread.yield();
		}
		state = ThreadState.RUNNING;
		sple = false;
	}

	public void _resume() {
		if (!sple)
			state = ThreadState.RUNNING;
	}

	public void finishExecution() {
		state = ThreadState.FINISHED;
	}
}