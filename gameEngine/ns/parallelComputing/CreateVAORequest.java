package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;

public class CreateVAORequest extends Request {

	private VAO vao;
	private VBOData[] data;

	public CreateVAORequest(VAO vao, VBOData[] args) {
		super("create vao", args);
		this.vao = vao;
		this.data = args;
	}

	@Override
	public void execute() {
		VAOLoader.createVAOAndStore(vao, data);
	}

}
