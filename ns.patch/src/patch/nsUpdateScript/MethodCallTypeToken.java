package patch.nsUpdateScript;

public class MethodCallTypeToken extends Token {
	public static final TokenPattern PATTERN = LANG_DEF.patterns.get("LANG_METHOD_CALL_TYPE_TOKEN");

	public MethodCallTypeToken() {
		super(PATTERN);
	}
}
