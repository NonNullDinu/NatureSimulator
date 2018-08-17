package patch.nsUpdateScript;

public class MethodCallArgumentsOpenDeclarationToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_CALL_ARGUMENTS_OPEN_DECLARATION_TOKEN");

	public MethodCallArgumentsOpenDeclarationToken() {
		super(PATTERN);
	}
}
