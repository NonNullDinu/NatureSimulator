package ns.exceptions;

public class GameException extends RuntimeException {
	private static final long serialVersionUID = 2404478978308821651L;
	private StackTraceElement[] stackTrace;

	public GameException(String message) {
		super(message);
	}

	public GameException(String message, StackTraceElement[] stackTrace) {
		super(message);
		this.stackTrace = stackTrace;
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return stackTrace == null ? super.getStackTrace() : stackTrace;
	}
}