package ns.parallelComputing;

import ns.interfaces.PAction;

public class SetRequest extends Request {

	private static PAction action;
	private final Object o;

	public SetRequest(Object o) {
		this.o = o;
	}

	public static void init(PAction action) {
		SetRequest.action = action;
	}

	@Override
	public void execute() {
		action.pexecute(o);
	}
}