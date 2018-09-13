package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;

public class CreateVAORequest extends Request {

	public VAO vao;
	private VBOData[] data;
	public CreateVAORequest(VAO vao, VBOData[] args) {
		this.vao = vao;
		this.data = args;
	}

	@Override
	public void execute() {
		VAOLoader.createVAOAndStore(vao, data);
	}

}
