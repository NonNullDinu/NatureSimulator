package patch.nsUpdateScript;

public class MethodBodyToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_BODY_TOKEN");

	public MethodBodyToken() {
		super(PATTERN);
	}
}
