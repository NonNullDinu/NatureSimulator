package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;

import java.util.List;

public class VBOUpdateRequest extends Request {

	private final VAO model;
	private final int attn;
	private final float[] data;
	private final List<Integer> changes;
	private final int dimensions;

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