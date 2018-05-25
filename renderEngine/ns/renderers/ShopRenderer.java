package ns.renderers;

import org.lwjgl.util.vector.Vector3f;

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
		if(!s.open())
			return;
		for (ShopItem item : s.getItems()) {
			fbo.bind();
			MasterRenderer.prepare();
			MasterRenderer.instance.render(item.getEntityBlueprint(), new Vector3f(0, 0, -100f));
			FBO.unbind();
			guiRenderer.bind();
			guiRenderer.batchRenderCall(item.getCenter(), ShopItem.SCALE, fbo.getTex());
			guiRenderer.unbind();
		}
	}
	
	public void cleanUp() {
		fbo.cleanUp();
	}
}