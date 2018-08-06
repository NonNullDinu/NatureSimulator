package ns.renderers;

import ns.exceptions.FBOAttachmentException;
import ns.openglObjects.FBO;
import ns.openglObjects.VAO;
import ns.shaders.ShaderProgram;

public abstract class EffectRenderer {
	protected ShaderProgram shader;
	protected VAO quad;
	
	public EffectRenderer(ShaderProgram shader, VAO quad) {
		this.shader = shader;
		this.quad = quad;
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	public abstract void apply(FBO source, FBO destination) throws FBOAttachmentException;
}