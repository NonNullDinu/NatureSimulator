package ns.exceptions;

public abstract class GameException extends RuntimeException {
	private static final long serialVersionUID = 2404478978308821651L;

	public GameException(String message) {
		super(message);
	}
}