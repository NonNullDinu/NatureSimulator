package ns.renderers;

import ns.exceptions.FBOAttachmentException;
import ns.openglObjects.FBO;
import ns.openglObjects.VAO;
import ns.shaders.ShaderProgram;

abstract class EffectRenderer {
	final VAO quad;
	private final ShaderProgram shader;

	public EffectRenderer(ShaderProgram shader, VAO quad) {
		this.shader = shader;
		this.quad = quad;
	}

	void cleanUp() {
		shader.cleanUp();
	}

	public abstract void apply(FBO source, FBO destination) throws FBOAttachmentException;
}