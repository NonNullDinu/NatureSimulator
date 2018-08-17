package patch.nsUpdateScript;

public class MethodNameDeclarationToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_NAME_DECLARATION_TOKEN");

	public MethodNameDeclarationToken() {
		super(PATTERN);
	}
}
