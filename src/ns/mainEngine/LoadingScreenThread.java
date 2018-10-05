package ns.mainEngine;

import data.GameData;
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
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class LoadingScreenThread implements Runnable {
	private static final float SPD = 0.01f;

	private float alphaCoef = 0.1f;
	private int textI = 0;
	public static boolean READY = false;
	private GUIText text;
	private boolean incr = true;
	private boolean toBreak;

	@Override
	public void run() {
		GU.currentThread().waitForGameDataInit();
		FontType z003 = new FontType(new TexFile("fonts/Z003.tex").load(),
				GU.open(GameData.getResourceAt("fonts/Z003.fnt")));
		GU.setZ003(z003);
		FontType caladea = new FontType(new TexFile("fonts/Caladea.tex").load(),
				GU.open(GameData.getResourceAt("fonts/Caladea.fnt")));
		GU.setCaladea(caladea);
		List<GUIText> textToShow = new ArrayList<>();
		textToShow.add(new GUIText("Made by", 5f, z003, new Vector2f(0.0f, 0.0f), 0.4f, true));
		textToShow.add(new GUIText("NonNullDinu", 3f, caladea, new Vector2f(0.0f, 0.0f), 0.4f, true));
		textToShow.add(new GUIText("Loading...", 2f, z003, new Vector2f(0.0f, 0.0f), 0.2f, true));
		text = textToShow.get(textI);
		TextMaster.loadText(text);
		text.setColour(0f, 0f, 0f);
		TextMaster.add(text);
		RenderMethod renderMethod = () -> {
			if (!READY) {
				addToAC(incr ? SPD : -SPD);
				if (alphaCoef >= 1.0)
					setIncr(false);
			} else {
				addToAC(1.0f - alphaCoef);
			}
			if (alphaCoef <= 0.0f) {
				if (READY) {
					setText(textToShow.get(textI));
					TextMaster.add(text);
					TextMaster.render(alphaCoef);
				} else {
					addToAC(-alphaCoef);
					text.remove();
					textI++;
					setReady(textI >= textToShow.size() - 1);
					setBreak(READY);
					setIncr(true);
					setText(textToShow.get(textI));
					TextMaster.add(text);
				}
			}
			TextMaster.render(alphaCoef);
		};
		GU.currentThread().finishLoading();
		DisplayManager.setWindowVisible();
		while (true) {
			GU.sendRequestToMainThread(
					new GLClearRequest(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, new Vector3f(1, 1, 1)));
			GU.sendRequestToMainThread(new GLRenderRequest(renderMethod));
			GU.sendRequestToMainThread(new UpdateDisplayRequest());
			for (int i = textI + 1; i < textToShow.size(); i++)
				TextMaster.loadTextIfNotLoadedAlready(textToShow.get(i));
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (MainGameLoop.state != GS.LOADING && READY)
				break;
		}
		TextMaster.remove(text);
		GU.currentThread().finishExecution();
	}

	private void setBreak(boolean b) {
		this.toBreak = b;
	}

	protected void setIncr(boolean b) {
		incr = b;
	}

	private void addToAC(float d) {
		alphaCoef += d;
	}

	private void setText(GUIText text) {
		this.text = text;
	}

	private void setReady(boolean b) {
		READY = b;
	}
}