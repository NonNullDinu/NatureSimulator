package ns.openglWorkers;

import ns.openglObjects.VAO;
import ns.parallelComputing.Request;

public class CreateAndPackVAORequest extends Request {

	private VBOData[] data;
	private VAO target;

	public CreateAndPackVAORequest(String request, VBOData[] args, VAO target) {
		super(request, args);
		this.data = args;
		this.target = target;
	}

	@Override
	public void execute() {
		VAOLoader.createVAOAndStorePack(target, data);
	}
}
