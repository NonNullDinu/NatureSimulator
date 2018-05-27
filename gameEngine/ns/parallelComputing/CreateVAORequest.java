package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;

public class CreateVAORequest extends Request {

	private VAO vao;
	private VBOData[] args;

	public CreateVAORequest(String request, VBOData[] args, VAO vao) {
		super(request, args);
		this.args = args;
		this.vao = vao;
	}

	@Override
	public void execute() {
		VAOLoader.createVAOAndStore(vao, args);
	}
}