package ns.fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ns.fontMeshCreator.FontType;
import ns.fontMeshCreator.GUIText;
import ns.fontMeshCreator.TextMeshData;
import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.renderers.FontRenderer;

public class TextMaster {
	private static Map<FontType, List<GUIText>> texts = new HashMap<>();
	private static List<GUIText> allTexts = new ArrayList<>();
	private static FontRenderer renderer;

	public static void init() {
		renderer = new FontRenderer();
	}

	public static void render() {
		renderer.render(texts);
	}

	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		VAO vao = VAOLoader.storeDataInVAO(
				new VBOData(data.getVertexPositions()).withAttributeNumber(0).withDimensions(2),
				new VBOData(data.getTextureCoords()).withAttributeNumber(1).withDimensions(2));
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
		allTexts.add(text);
	}

	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

	public static void cleanUp() {
		renderer.cleanUp();
		for(GUIText text : allTexts) {
			text.getMesh().delete();
		}
	}
}