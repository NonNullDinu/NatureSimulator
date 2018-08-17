package patch.nsUpdateScript;

public class MethodCallToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_CALL_TOKEN");

	public MethodCallToken() {
		super(PATTERN);
	}
}
