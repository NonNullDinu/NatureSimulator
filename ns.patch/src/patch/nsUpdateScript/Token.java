package patch.nsUpdateScript;

public abstract class Token {
	protected TokenPattern format;

	public Token(TokenPattern format) {
		this.format = format;
	}
}
