package ns.mainEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.customFileFormat.TexFile;
import ns.display.DisplayManager;
import ns.fontMeshCreator.FontType;
import ns.fontMeshCreator.GUIText;
import ns.fontRendering.TextMaster;
import ns.parallelComputing.GLClearRequest;
import ns.parallelComputing.GLRenderRequest;
import ns.parallelComputing.GLRenderRequest.RenderMethod;
import ns.parallelComputing.UpdateDisplayRequest;
import ns.utils.GU;
import res.Resource;

public class LoadingScreenThread implements Runnable {
	@Override
	public void run() {
		int prevFrame = DisplayManager.frameId - 1;
		GU.currentThread().waitForDisplayInit();
		FontType z003 = new FontType(new TexFile("fonts/Z003.tex").load(),
				GU.open(new Resource().withLocation("fonts/Z003.fnt").create()));
		GU.setZ003(z003);
		FontType caladea = new FontType(new TexFile("fonts/Caladea.tex").load(),
				GU.open(new Resource().withLocation("fonts/Caladea.fnt").create()));
		GU.setCaladea(caladea);
		GUIText text = new GUIText("Loading...", 2f, z003, new Vector2f(0.0f, 0.0f), 0.2f, true);
//		GUIText textCopyright = new GUIText("Copyright (c) 2018, NonNullDinu", 1f, caladea, new Vector2f(0.4f, -0.7f), 0.4f, true);
		TextMaster.loadText(text);
//		TextMaster.loadText(textCopyright);
		text.setColour(0f, 0f, 0f);
		TextMaster.add(text);
//		TextMaster.add(textCopyright);
		RenderMethod renderMethod = new RenderMethod() {
			@Override
			public void render() {
				TextMaster.render();
			}
		};
		GU.currentThread().finishLoading();
		while (true) {
			if (prevFrame < DisplayManager.frameId) {
				prevFrame = DisplayManager.frameId;
				GU.sendRequestToMainThread(
						new GLClearRequest(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, new Vector3f(1, 1, 1)));
				GU.sendRequestToMainThread(new GLRenderRequest(renderMethod));
				GU.sendRequestToMainThread(new UpdateDisplayRequest());
			}
			if (MainGameLoop.state != GS.LOADING)
				break;
		}
		TextMaster.remove(text);
//		TextMaster.remove(textCopyright);
	}
}