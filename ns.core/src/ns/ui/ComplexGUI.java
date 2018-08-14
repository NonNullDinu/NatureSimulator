package ns.ui;

import ns.openglObjects.IRenderable;
import ns.renderers.GUIRenderer;
import ns.renderers.QuadRenderer;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class ComplexGUI implements IRenderable {
	private Vector2f center;
	private Vector2f scale;
	private List<GUITexture> others;
	private GUIRenderer guiRenderer;

	public ComplexGUI(Vector2f center, Vector2f scale, List<GUITexture> others, GUIRenderer guiRenderer) {
		this.center = center;
		this.scale = scale;
		this.others = others;
		this.guiRenderer = guiRenderer;
	}

	public Vector2f getCenter() {
		return center;
	}

	public Vector2f getScale() {
		return scale;
	}

	public List<GUITexture> getOthers() {
		return others;
	}

	@Override
	public void render() {
		QuadRenderer.render(center, scale, new Vector3f(0.5f, 0.5f, 0.5f));
		guiRenderer.render(others);
	}

	@Override
	public void batchRenderCall() {
		render();
	}
}