package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;

public class CreateAndPackVAORequest extends Request {

	private VBOData[] data;
	private VAO target;

	public CreateAndPackVAORequest(VBOData[] args, VAO target) {
		this.data = args;
		this.target = target;
	}

	@Override
	@Deprecated
	public void execute() {
		VAOLoader.createVAOAndStorePack(target, data);
	}
}
