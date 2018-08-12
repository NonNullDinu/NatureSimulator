package ns.exceptions;

import java.io.IOException;

public class LoadingException extends GameException {
	private static final long serialVersionUID = 1320557239316445228L;

	public LoadingException(String message) {
		super(message);
	}

	public LoadingException(IOException e) {
		super(e.getMessage(), e.getStackTrace());
	}
}