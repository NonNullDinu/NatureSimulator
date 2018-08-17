package patch.nsUpdateScript;

public class MethodDeclarationToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_DECLARATION_TOKEN");
	protected String name;

	public MethodDeclarationToken(String name) {
		super(PATTERN);
		this.name = name;
	}

	public static MethodDeclarationToken get(String string) {
		return LANG_DEF.breakToTokensMDT(string);
	}
}
