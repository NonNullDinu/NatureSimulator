package ns.parallelComputing;

import ns.interfaces.PAction;

public class SetRequest extends Request {

	private final Object o;
	
	private static PAction action;
	
	public static void init(PAction action) {
		SetRequest.action = action;
	}

	public SetRequest(Object o) {
		this.o = o;
	}

	@Override
	public void execute() {
		action.pexecute(o);
	}
}