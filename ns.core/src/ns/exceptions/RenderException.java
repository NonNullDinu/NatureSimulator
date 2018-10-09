package ns.exceptions;

abstract class RenderException extends RuntimeException {
	private static final long serialVersionUID = -8430216383031558786L;

	public RenderException(String message) {
		super(message);
	}
}