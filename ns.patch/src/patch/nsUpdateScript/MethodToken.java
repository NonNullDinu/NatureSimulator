package patch.nsUpdateScript;

public class MethodToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_TOKEN");

	public MethodToken() {
		super(PATTERN);
	}
}
