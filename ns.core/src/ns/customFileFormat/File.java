package ns.customFileFormat;

import ns.exceptions.LoadingException;

public interface File {

	Object load() throws LoadingException;
}