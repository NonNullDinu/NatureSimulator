/*
 * Copyright (C) 2018  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.fontMeshCreator;

import ns.fontRendering.TextMaster;
import ns.openglObjects.VAO;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a piece of text in the game.
 *
 * @author Karl
 */
public class GUIText {

	private final float fontSize;
	private final Vector3f colour = new Vector3f(0f, 0f, 0f);
	private final Vector2f position;
	private final float lineMaxSize;
	private final FontType font;
	private String textString;
	private VAO textMeshVao;
	private int vertexCount;
	private int numberOfLines;
	private boolean centerText;

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 *
	 * @param text          - the text.
	 * @param fontSize      - the font size of the text, where a font size of 1 is the
	 *                      default size.
	 * @param font          - the font that this text should use.
	 * @param position      - the position on the screen where the top left corner of the
	 *                      text should be rendered. The top left corner of the screen is
	 *                      (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength - basically the width of the virtual page in terms of screen
	 *                      width (1 is full screen width, 0.5 is half the width of the
	 *                      screen, etc.) Text cannot go off the edge of the page, so if
	 *                      the text is longer than this length it will go onto the next
	 *                      line. When text is centered it is centered into the middle of
	 *                      the line, based on this line length value.
	 * @param centered      - whether the text should be centered or not.
	 */
	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
	               boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		setColour(0, 0, 0);
	}

	/**
	 * Remove the text from the screen.
	 */
	public void remove() {
		TextMaster.remove(this);
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the colour of the text.
	 *
	 * @param r - red value, between 0 and 1.
	 * @param g - green value, between 0 and 1.
	 * @param b - blue value, between 0 and 1.
	 */
	public void setColour(float r, float g, float b) {
		colour.set(r, g, b);
	}

	/**
	 * @return the colour of the text.
	 */
	public Vector3f getColour() {
		return colour;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 * loaded, based on the length of the text and the max line length
	 * that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in
	 * loading).
	 *
	 * @param number
	 */
	void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space.
	 * (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 * right.
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @return the ID of the text's VAO, which contains all the vertex data for
	 * the quads on which the text will be rendered.
	 */
	public VAO getMesh() {
		return textMeshVao;
	}

	/**
	 * Set the VAO and vertex count for this text.
	 *
	 * @param vao           - the VAO containing all the vertex data for the quads on
	 *                      which the text will be rendered.
	 * @param verticesCount - the total number of vertices in all of the quads.
	 */
	public void setMeshInfo(VAO vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	/**
	 * @return The total number of vertices of all the text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	float getFontSize() {
		return fontSize;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	public String getTextString() {
		return textString;
	}

	public void setText(String text) {
		this.textString = text;
	}
}