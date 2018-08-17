package patch.nsUpdateScript;

public class MethodCallArgumentsToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_CALL_ARGUMENTS_TOKEN");

	public MethodCallArgumentsToken() {
		super(PATTERN);
	}
}
