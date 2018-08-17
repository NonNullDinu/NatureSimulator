package patch.nsUpdateScript;

public class CodeBlockOpenBracketToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_CODE_BLOCK_OPEN_BRACKET_TOKEN");

	public CodeBlockOpenBracketToken() {
		super(PATTERN);
	}
}
