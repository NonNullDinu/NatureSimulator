package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VBOUpdateData;

public class VAOUpdateRequest extends Request {

	private VAO vao;
	private VBOUpdateData data;

	public VAOUpdateRequest(VAO vao, VBOUpdateData data) {
		this.vao = vao;
		this.data = data;
	}

	@Override
	public void execute() {
		data.updateWithin(vao);
	}
}