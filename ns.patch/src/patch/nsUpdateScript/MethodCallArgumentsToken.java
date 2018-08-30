package patch.nsUpdateScript;

public class MethodCallArgumentsToken extends Token {
	private final MethodCallArgumentToken[] arguments;

	public MethodCallArgumentsToken(MethodCallArgumentToken... arguments) {
		this.arguments = arguments;
	}
}
