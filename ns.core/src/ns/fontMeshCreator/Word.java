package ns.fontMeshCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * During the loading of a text this represents one word in the text.
 * 
 * @author Karl
 *
 */
class Word {

	private final List<Character> characters = new ArrayList<>();
	private double width = 0;
	private final double fontSize;
	private final double space;

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
