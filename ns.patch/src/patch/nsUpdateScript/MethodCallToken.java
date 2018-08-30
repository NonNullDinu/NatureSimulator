package patch.nsUpdateScript;

public class MethodCallToken extends Token {
	private final MethodCallArgumentsToken arg;
	private final Method methodToCall;

	public MethodCallToken(Method methodToCall, MethodCallArgumentsToken argumentsToken) {
		this.methodToCall = methodToCall;
		this.arg = argumentsToken;
	}
}
