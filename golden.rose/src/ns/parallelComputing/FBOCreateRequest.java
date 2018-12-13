package ns.parallelComputing;

import ns.openglObjects.FBO;

public class FBOCreateRequest extends Request {
	private FBO fbo;

	public FBOCreateRequest(FBO fbo) {
		this.fbo = fbo;
	}

	@Override
	public void execute() {
		fbo.create();
	}
}