package ns.parallelComputing;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;

public class VBORecreateAndReplaceRequest extends Request {

	private final VAO model;
	private final int attn;
	private final float[] dataf;
	private final byte[] datab;
	private final int usage;

	public VBORecreateAndReplaceRequest(VAO model, int attn, float[] data, int usage) {
		this.model = model;
		this.attn = attn;
		this.dataf = data;
		this.datab = null;
		this.usage = usage;
	}

	public VBORecreateAndReplaceRequest(VAO model, int attn, byte[] data, int usage) {
		this.model = model;
		this.attn = attn;
		this.datab = data;
		this.dataf = null;
		this.usage = usage;
	}

	@Override
	public void execute() {
		if (dataf == null)
			VAOLoader.recreateAndReplace(model, attn, datab, usage);
		else
			VAOLoader.recreateAndReplace(model, attn, dataf, usage);
	}
}