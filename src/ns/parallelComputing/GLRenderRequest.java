package ns.parallelComputing;

public class GLRenderRequest extends Request {

	public static interface RenderMethod {
		public abstract void render();
	}

	private RenderMethod renderMethod;

	public GLRenderRequest(RenderMethod renderMethod) {
		this.renderMethod = renderMethod;
	}

	@Override
	public void execute() {
		renderMethod.render();
	}
}