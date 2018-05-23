package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VBOUpdateData;

public class VAOUpdateRequest extends Request {

	private VAO vao;
	private VBOUpdateData data;

	public VAOUpdateRequest(String request, VBOUpdateData arg, VAO vao) {
		super(request, new Object[] { arg });
		this.vao = vao;
		this.data = arg;
	}

	@Override
	public void execute() {
		data.updateWithin(vao);
	}
}