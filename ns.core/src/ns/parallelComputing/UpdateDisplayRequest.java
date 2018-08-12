package ns.parallelComputing;

import ns.display.DisplayManager;

public class UpdateDisplayRequest extends Request {

	public UpdateDisplayRequest() {
	}

	@Override
	public void execute() {
		DisplayManager.updateDisplay();
	}
}