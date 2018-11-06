package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;

public class VBORecreateAndReplaceRequest extends Request {

	private final VAO model;
	private final int attn;
	private final float[] data;
	private final int usage;

	public VBORecreateAndReplaceRequest(VAO model, int attn, float[] data, int usage) {
		this.model = model;
		this.attn = attn;
		this.data = data;
		this.usage = usage;
	}

	@Override
	public void execute() {
		VAOLoader.recreateAndReplace(model, attn, data, usage);
	}
}