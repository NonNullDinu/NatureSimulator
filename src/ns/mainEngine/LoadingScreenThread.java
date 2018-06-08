package ns.mainEngine;

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
import ns.parallelComputing.ThreadMaster;
import ns.parallelComputing.UpdateDisplayRequest;
import ns.utils.GU;
import res.Resource;

public class LoadingScreenThread implements Runnable {
	public static boolean REQUESTED_TEXT_CREATION;

	@Override
	public void run() {
		int prevFrame = DisplayManager.frameId - 1;
		FontType font = new FontType(new Texture("res/fonts/Z003.png").create(),
				GU.open(new Resource().withLocation("res/fonts/Z003.fnt").create()));
		GUIText text = new GUIText("Loading screen", 2f, font, new Vector2f(0.0f, 0.0f), 0.2f, true);
		text.setColour(1f, 1f, 1f);
		RenderMethod renderMethod = new RenderMethod() {
			@Override
			public void render() {
				TextMaster.render();
			}
		};
		Thread.yield();
		TextMaster.loadText(text);
		// Load loading screen resources
		Thread t = ThreadMaster.getThread("main thread");
		while (true) {
			if (prevFrame < DisplayManager.frameId) {
				prevFrame = DisplayManager.frameId;
				t.setToCarryOutRequest(
						new GLClearRequest(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, new Vector3f(0, 0, 0)));
				t.setToCarryOutRequest(new GLRenderRequest(renderMethod));
				t.setToCarryOutRequest(new UpdateDisplayRequest());
			}
			Thread.yield();
			if (MainGameLoop.state != GS.LOADING)
				break;
		}
	}
}