package patch.nsUpdateScript;

public class MethodDeclarationToken extends Token {
	protected String name;

	public MethodDeclarationToken(String name) {
		this.name = name;
	}

	public static MethodDeclarationToken get(String string) {
		return LANG_DEF.breakToTokensMDT(string);
	}
}
