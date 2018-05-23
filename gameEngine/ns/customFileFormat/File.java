package ns.customFileFormat;

import ns.exceptions.LoadingException;

public interface File {
	
	public abstract Object load() throws LoadingException;
}