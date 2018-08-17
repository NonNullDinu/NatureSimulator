package patch.nsUpdateScript;

public class MethodCallNameToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_CALL_NAME_TOKEN");

	public MethodCallNameToken() {
		super(PATTERN);
	}
}
