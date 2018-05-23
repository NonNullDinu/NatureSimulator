package ns.exceptions;

public abstract class LoadingException extends GameException {
	private static final long serialVersionUID = 1320557239316445228L;

	public LoadingException(String message) {
		super(message);
	}
}