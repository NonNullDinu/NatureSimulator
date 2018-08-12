package ns.exceptions;

import ns.openglObjects.FBO;

/**
 * Thrown when attempting to render an effect from a FBO that doesn't have the right configuration
 * @author Dinu B.
 */
public class FBOAttachmentException extends RenderException {
	private static final long serialVersionUID = -4759771195837122461L;

	public FBOAttachmentException(String message) {
		super(message);
	}
	
	public FBOAttachmentException(FBO fbo) {
		this("The source got wrong configuration:" + fbo + " - configuration:" + fbo.getConfig());
	}
}