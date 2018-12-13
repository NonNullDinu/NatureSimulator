package ns.shaders;

import org.lwjgl.opengl.GL20;

public class GUIShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "guis/guiVertex.glsl";
	private static final String FRAGMENT_SHADER = "guis/guiFragment.glsl";

	public final UniformMat4 transformationMatrix = new UniformMat4("transformationMatrix");

	public final UniformFloat alpha = new UniformFloat("alpha");

	public GUIShader() {
		super(new Shader(VERTEX_SHADER, GL20.GL_VERTEX_SHADER), new Shader(FRAGMENT_SHADER, GL20.GL_FRAGMENT_SHADER));
		storeUniforms(transformationMatrix, alpha);
	}

	public void fillOtherArgs(float[] others) {
		if (others == null) {
			alpha.load(1);
			return;
		}
		float val = 0;
		for (int i = 0; i < 1; i++) {
			if (i >= others.length) {
				if (i == 0)
					val = 1;
			} else val = others[i];
			switch (i) {
				case 0:
					alpha.load(val);
					break;
			}
		}
	}
}