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

import java.util.ArrayList;
import java.util.List;

/**
 * During the loading of a text this represents one word in the text.
 *
 * @author Karl
 */
class Word {

	private final List<Character> characters = new ArrayList<>();
	private final double fontSize;
	private final double space;
	private double width = 0;

	/**
	 * Create a new empty word.
	 *
	 * @param fontSize - the font size of the text which this word is in.
	 * @param d
	 */
	protected Word(double fontSize, double d) {
		this.fontSize = fontSize;
		this.space = d;
	}

	/**
	 * Adds a character to the end of the current word and increases the
	 * screen-space width of the word.
	 *
	 * @param character - the character to be added.
	 */
	void addCharacter(Character character) {
		characters.add(character);
		if (character.getId() == '\t') {
			width += TextMeshCreator.TAB_SPC * space * fontSize;
		} else
			width += character.getxAdvance() * fontSize;
	}

	/**
	 * @return The list of characters in the word.
	 */
	List<Character> getCharacters() {
		return characters;
	}

	/**
	 * @return The width of the word in terms of screen size.
	 */
	double getWordWidth() {
		return width;
	}

}
