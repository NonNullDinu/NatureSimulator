package ns.parallelComputing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class Thread extends java.lang.Thread {
	public List<Request> toCarryOutRequests = new ArrayList<>();
	public List<CreateVAORequest> vaoCreateRequests = new ArrayList<>();

	public Thread(String name, Runnable runnable) {
		super(runnable, name);
	}

	public void setToCarryOutRequest(Request request) {
		if(request instanceof CreateVAORequest) {
			vaoCreateRequests.add((CreateVAORequest) request);
			return;
		}
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
		this.toCarryOutRequests.clear();
		this.vaoCreateRequests.clear();
	}
}