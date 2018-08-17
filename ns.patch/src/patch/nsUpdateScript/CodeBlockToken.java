package patch.nsUpdateScript;

public class CodeBlockToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_CODE_BLOCK_TOKEN");

	public CodeBlockToken() {
		super(PATTERN);
	}
}
