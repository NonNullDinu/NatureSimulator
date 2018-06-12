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
	public static boolean initialized = false;

	public static void init() {
		renderer = new FontRenderer();
		initialized = true;
	}

	public static void render() {
		renderer.render(texts);
	}

	public static void loadText(GUIText text) {
		load(text);
	}
	
	private static void load(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		VAO vao = VAOLoader.storeDataInVAO(
				new VBOData(data.getVertexPositions()).withAttributeNumber(0).withDimensions(2),
				new VBOData(data.getTextureCoords()).withAttributeNumber(1).withDimensions(2));
		text.setMeshInfo(vao, data.getVertexCount());
		allTexts.add(0, text);
	}

	public static void cleanUp() {
		renderer.cleanUp();
		for(GUIText text : allTexts) {
			text.getMesh().delete();
		}
	}
	
	public static void add(GUIText text) {
		FontType font = text.getFont();
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void remove(GUIText text) {
		FontType font = text.getFont();
		
		List<GUIText> textBatch = texts.get(font);
		textBatch.remove(text);
		if(textBatch.isEmpty())
			texts.remove(font);
	}

	public static void delete(GUIText text) {
		text.getMesh().delete();
	}
}