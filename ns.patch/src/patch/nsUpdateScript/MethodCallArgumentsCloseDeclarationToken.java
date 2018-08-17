package patch.nsUpdateScript;

public class MethodCallArgumentsCloseDeclarationToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_CALL_ARGUMENTS_CLOSE_DECLARATION");

	public MethodCallArgumentsCloseDeclarationToken() {
		super(PATTERN);
	}
}
