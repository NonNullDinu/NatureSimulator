package ns.parallelComputing;

public class GLRenderRequest extends Request {

	private final RenderMethod renderMethod;

	public GLRenderRequest(RenderMethod renderMethod) {
		this.renderMethod = renderMethod;
	}

	@Override
	public void execute() {
		renderMethod.render();
	}

	public interface RenderMethod {
		void render();
	}
}