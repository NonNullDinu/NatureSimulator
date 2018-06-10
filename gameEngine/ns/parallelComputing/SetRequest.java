package ns.parallelComputing;

import ns.mainEngine.MainGameLoop;

public class SetRequest extends Request {

	private Object o;

	public SetRequest(Object o) {
		super("set " + o.getClass().getName(), null);
		this.o = o;
	}

	@Override
	public void execute() {
		((MainGameLoop) ((Thread) java.lang.Thread.currentThread()).getRunnable()).set(o);
	}
}