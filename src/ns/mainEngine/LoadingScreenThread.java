package ns.mainEngine;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.display.DisplayManager;
import ns.fontMeshCreator.FontType;
import ns.fontMeshCreator.GUIText;
import ns.fontRendering.TextMaster;
import ns.openglObjects.Texture;
import ns.parallelComputing.GLClearRequest;
import ns.parallelComputing.GLRenderRequest;
import ns.parallelComputing.GLRenderRequest.RenderMethod;
import ns.parallelComputing.Thread;
import ns.parallelComputing.UpdateDisplayRequest;
import ns.utils.GU;
import res.Resource;

public class LoadingScreenThread implements Runnable {
	@Override
	public void run() {
		int prevFrame = DisplayManager.frameId - 1;
		while(!Display.isCreated())
			Thread.yield();
		FontType font = new FontType(new Texture("res/fonts/Caladea.png").create(),
				GU.open(new Resource().withLocation("res/fonts/Caladea.fnt").create()));
		GU.setZ003(font);
		GUIText text = new GUIText("Loading...", 2f, font, new Vector2f(0.0f, 0.0f), 0.2f, true);
		TextMaster.loadText(text);
		text.setColour(0f, 0f, 0f);
		TextMaster.add(text);
		RenderMethod renderMethod = new RenderMethod() {
			@Override
			public void render() {
				TextMaster.render();
			}
		};
		GU.currentThread().finishLoading();
		Thread.yield();
		while (true) {
			if (prevFrame < DisplayManager.frameId) {
				prevFrame = DisplayManager.frameId;
				GU.sendRequestToMainThread(
						new GLClearRequest(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, new Vector3f(1, 1, 1)));
				GU.sendRequestToMainThread(new GLRenderRequest(renderMethod));
				GU.sendRequestToMainThread(new UpdateDisplayRequest());
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (MainGameLoop.state != GS.LOADING)
				break;
		}
		TextMaster.remove(text);
	}
}