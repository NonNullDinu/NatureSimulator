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

/**
 * Simple data structure class holding information about a certain glyph in the
 * font texture atlas. All sizes are for a font-size of 1.
 *
 * @author Karl
 */
public class Character {
	public static final Character TAB = new Character('\t', 0, 0, 0, 0, 0, 0, 0, 0, 0);

	private final int id;
	private final double xTextureCoord;
	private final double yTextureCoord;
	private final double xMaxTextureCoord;
	private final double yMaxTextureCoord;
	private final double xOffset;
	private final double yOffset;
	private final double sizeX;
	private final double sizeY;
	private final double xAdvance;

	/**
	 * @param id            - the ASCII value of the character.
	 * @param xTextureCoord - the x texture coordinate for the top left corner of the
	 *                      character in the texture atlas.
	 * @param yTextureCoord - the y texture coordinate for the top left corner of the
	 *                      character in the texture atlas.
	 * @param xTexSize      - the width of the character in the texture atlas.
	 * @param yTexSize      - the height of the character in the texture atlas.
	 * @param xOffset       - the x distance from the curser to the left edge of the
	 *                      character's quad.
	 * @param yOffset       - the y distance from the curser to the top edge of the
	 *                      character's quad.
	 * @param sizeX         - the width of the character's quad in screen space.
	 * @param sizeY         - the height of the character's quad in screen space.
	 * @param xAdvance      - how far in pixels the cursor should advance after adding
	 *                      this character.
	 */
	protected Character(int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize,
	                    double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
		this.id = id;
		this.xTextureCoord = xTextureCoord;
		this.yTextureCoord = yTextureCoord;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.xMaxTextureCoord = xTexSize + xTextureCoord;
		this.yMaxTextureCoord = yTexSize + yTextureCoord;
		this.xAdvance = xAdvance;
	}

	int getId() {
		return id;
	}

	double getxTextureCoord() {
		return xTextureCoord;
	}

	double getyTextureCoord() {
		return yTextureCoord;
	}

	double getXMaxTextureCoord() {
		return xMaxTextureCoord;
	}

	double getYMaxTextureCoord() {
		return yMaxTextureCoord;
	}

	double getxOffset() {
		return xOffset;
	}

	double getyOffset() {
		return yOffset;
	}

	double getSizeX() {
		return sizeX;
	}

	double getSizeY() {
		return sizeY;
	}

	double getxAdvance() {
		return xAdvance;
	}

}
