package ns.parallelComputing;

import data.GameData;
import ns.interfaces.Condition;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

public class Thread extends java.lang.Thread {
	public enum ThreadState {
		WAITING, RUNNING, FINISHED
	}

	public List<Request> toCarryOutRequests = new ArrayList<>();
	public List<CreateVAORequest> vaoCreateRequests = new ArrayList<>();
	private Runnable runnable;
	public List<Request> renderingRequests = new ArrayList<>();
	private long timeb;
	public boolean isExecutingRequests;
	private ThreadState state;
	private boolean special_loop_executing;

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

	public void waitForGameDataInit() {
		while (!GameData.initialized())
			java.lang.Thread.yield();
	}

	public void sayTimeSinceBegin() {
		System.out.println(getName() + " ran for " + (System.nanoTime() - timeb));
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
		special_loop_executing = true;
		state = ThreadState.WAITING;
		while (c.value()) {
			Thread.yield();
		}
		state = ThreadState.RUNNING;
		special_loop_executing = false;
	}

	public void _resume() {
		if (!special_loop_executing)
			state = ThreadState.RUNNING;
	}

	public void finishExecution() {
		state = ThreadState.FINISHED;
	}
}