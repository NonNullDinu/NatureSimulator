package patch.nsUpdateScript;

public class CodeBlockCloseBracketToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_CODE_BLOCK_CLOSE_BRACKET_TOKEN");

	public CodeBlockCloseBracketToken() {
		super(PATTERN);
	}
}
