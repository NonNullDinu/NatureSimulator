package ns.ui.loading;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ns.components.BlueprintCreator;
import ns.customFileFormat.TexFile;
import ns.entities.Entity;
import ns.fontMeshCreator.FontType;
import ns.fontMeshCreator.GUIText;
import ns.interfaces.Action;
import ns.interfaces.UIMenu;
import ns.mainMenu.MainMenu;
import ns.mainMenu.MainMenuButton;
import ns.openglObjects.Texture;
import ns.options.OnOffOption;
import ns.options.Option;
import ns.options.Options;
import ns.ui.GUIButton;
import ns.utils.GU;

public class UILoader {
	private static DocumentBuilder builder;
	private static Action[] actions;
	
	public static void init(Action[] actions) {
		UILoader.actions = actions;
	}

	static {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setValidating(true);
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static UIMenu load(InputStream in) {
		Document document = getDocument(in);
		UIMenu menu = null;
		if (document.getDoctype().getName().equals("MainMenu")) {
			List<MainMenuButton> buttons = new ArrayList<>();
			float dnaX = 0, dnaY = 0, dnaZ = 0;
			NodeList nodes = document.getDocumentElement().getChildNodes();
			Vector4f dnaLocation = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().equals("MainMenuButton")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					Action action = null;
					int ac = Integer.parseInt(nodeMap.getNamedItem("action").getNodeValue());
					action = actions[ac - 1];
					Texture texture = new TexFile(nodeMap.getNamedItem("texture").getNodeValue()).load();
					buttons.add(new MainMenuButton(new Vector2f(x, y), new Vector2f(xScale, yScale), action, texture));
				} else if (node.getNodeName().equals("DNAPos")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					dnaLocation = new Vector4f(x, y, xScale, yScale);
					dnaX = Float.parseFloat(nodeMap.getNamedItem("dnaX").getNodeValue());
					dnaY = Float.parseFloat(nodeMap.getNamedItem("dnaY").getNodeValue());
					dnaZ = Float.parseFloat(nodeMap.getNamedItem("dnaZ").getNodeValue());
				}
			}
			menu = new MainMenu(buttons,
					new Entity(BlueprintCreator.createModelBlueprintFor("menuDNA"), new Vector3f(dnaX, dnaY, dnaZ)),
					dnaLocation);
		} else if (document.getDoctype().getName().equals("Options")) {
			NodeList nodes = document.getDocumentElement().getChildNodes();
			GUIButton backButton = null;
			List<Option> options = new ArrayList<>();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().equals("BackButton")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					Texture texture = new TexFile(nodeMap.getNamedItem("texture").getNodeValue()).load();
					backButton = new GUIButton(new Vector2f(x, y), new Vector2f(xScale, yScale), texture);
				} else if (node.getNodeName().equals("OnOffOption")) {
					NamedNodeMap nodeMap = node.getAttributes();
					float x = Float.parseFloat(nodeMap.getNamedItem("x").getNodeValue());
					float y = Float.parseFloat(nodeMap.getNamedItem("y").getNodeValue());
					float xScale = Float.parseFloat(nodeMap.getNamedItem("xScale").getNodeValue());
					float yScale = Float.parseFloat(nodeMap.getNamedItem("yScale").getNodeValue());
					Node textNode = node.getChildNodes().item(0);
					NamedNodeMap textMap = textNode.getAttributes();
					x = Float.parseFloat(textMap.getNamedItem("x").getNodeValue());
					y = Float.parseFloat(textMap.getNamedItem("y").getNodeValue());
					String font = textMap.getNamedItem("font").getNodeValue();
					float fontSize = Float.parseFloat(textMap.getNamedItem("fontSize").getNodeValue());
					float maxLen = Float.parseFloat(textMap.getNamedItem("maxLen").getNodeValue());
					FontType realFont = (font.equals("Z003") ? GU.Z003 : GU.caladea);
					GUIText text = new GUIText(textMap.getNamedItem("text").getNodeValue(), fontSize, realFont,
							new Vector2f(x, y), maxLen, true);
					options.add(new OnOffOption(new Vector2f(x, y), new Vector2f(xScale, yScale), text));
				}
			}
			menu = new Options(options, backButton, actions[actions.length - 1]);
		}
		return menu;
	}

	private static Document getDocument(InputStream in) {
		try {
			return builder.parse(in);
		} catch (IOException | SAXException e) {
			e.printStackTrace();
		}
		return null;
	}
}