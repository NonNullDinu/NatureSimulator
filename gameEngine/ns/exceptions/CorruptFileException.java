package ns.exceptions;

public class CorruptFileException extends LoadingException {
	private static final long serialVersionUID = -2524782042036254798L;

	public CorruptFileException(String message) {
		super(message);
	}
}