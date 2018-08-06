package ns.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.components.Blueprint;
import ns.openglObjects.FBO;
import ns.ui.shop.Shop;
import ns.ui.shop.ShopItem;

public class ShopRenderer {
	private GUIRenderer guiRenderer;
	private FBO fbo;

	public ShopRenderer(GUIRenderer guiRenderer) {
		this.guiRenderer = guiRenderer;
		this.fbo = new FBO(128, 128, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
	}

	public void render(Shop s) {
		if (!s.open())
			return;
		QuadRenderer.render(s.getComplex().getCenter(), s.getComplex().getScale(), new Vector3f(0.5f, 0.5f, 0.5f));
		for (ShopItem item : s.getItems()) {
			fbo.bind();
			if (item.isMouseOver())
				GL11.glClearColor(0.8f, 0.8f, 0.8f, 1f);
			else
				GL11.glClearColor(0.5f, 0.5f, 0.5f, 1f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			Blueprint blueprint = item.getEntityBlueprint();
			boolean shouldScale = blueprint.getModel().shouldScale();
			MasterRenderer.instance.render(blueprint, new Vector3f(0, shouldScale ? -1f : -3f, shouldScale ? -6.6f : -20f));
			FBO.unbind();
			guiRenderer.bind();
			guiRenderer.batchRenderCall(Vector2f.add(s.getComplex().getCenter(), item.getCenter(), null),
					ShopItem.SCALE, fbo.getTex());
			guiRenderer.unbind();
		}
	}

	public void cleanUp() {
		fbo.cleanUp();
	}
}