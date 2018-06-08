package ns.parallelComputing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class Thread extends java.lang.Thread {
	public List<Request> toCarryOutRequests = new ArrayList<>();
	public List<CreateVAORequest> vaoCreateRequests = new ArrayList<>();
	private Runnable runnable;
	public List<Request> renderingRequests = new ArrayList<>();

	protected Thread(String name, Runnable runnable) {
		super(runnable, name);
		this.runnable = runnable;
	}

	public void setToCarryOutRequest(Request request) {
		if (request instanceof CreateVAORequest)
			vaoCreateRequests.add((CreateVAORequest) request);
		else if (request instanceof GLClearRequest || request instanceof GLRenderRequest || request instanceof UpdateDisplayRequest) {
			renderingRequests.add(request);
		} else
			this.toCarryOutRequests.add(request);
	}

	public <T> T exchange(T toExchange) {
		Exchanger<T> exchanger = new Exchanger<>();
		try {
			return exchanger.exchange(toExchange);
		} catch (InterruptedException e) {
		}
		return null;
	}

	public void clearRequests() {
		toCarryOutRequests.clear();
		vaoCreateRequests.clear();
		renderingRequests.clear();
	}

	public Runnable getRunnable() {
		return runnable;
	}
}