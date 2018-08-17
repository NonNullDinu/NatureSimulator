package patch.nsUpdateScript;

public class MethodCallArgumentToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_CALL_ARGUMENT_TOKEN");

	public MethodCallArgumentToken() {
		super(PATTERN);
	}
}
