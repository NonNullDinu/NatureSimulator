package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;

import java.util.List;

public class VBOUpdateRequest extends Request {

	private VAO model;
	private int attn;
	private float[] data;
	private List<Integer> changes;
	private int dimensions;

	public VBOUpdateRequest(VAO model, int attn, float[] data, List<Integer> changes, int dimensions) {
		this.model = model;
		this.attn = attn;
		this.data = data;
		this.changes = changes;
		this.dimensions = dimensions;
	}

	@Override
	public void execute() {
		VAOLoader.replace(model, attn, data, changes, dimensions);
	}
}