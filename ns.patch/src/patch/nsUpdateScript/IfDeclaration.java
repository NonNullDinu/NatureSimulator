package patch.nsUpdateScript;

public class IfDeclaration extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_IF_DECLARATION");

	public IfDeclaration() {
		super(PATTERN);
	}
}
