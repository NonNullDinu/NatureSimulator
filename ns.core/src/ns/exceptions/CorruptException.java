package ns.exceptions;

public class CorruptException extends LoadingException {
	private static final long serialVersionUID = -2524782042036254798L;

	public CorruptException(String message) {
		super(message);
	}
}