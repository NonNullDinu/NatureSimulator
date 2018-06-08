package ns.parallelComputing;

import ns.display.DisplayManager;

public class UpdateDisplayRequest extends Request {

	public UpdateDisplayRequest() {
		super("update display", null);
	}

	@Override
	public void execute() {
		DisplayManager.updateDisplay();
	}
}