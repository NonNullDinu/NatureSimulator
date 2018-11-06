package ns.water;

import ns.openglObjects.FBO;

public class WaterFBOs {
	private static final int REFLECTION_WIDTH = 2048, REFRACTION_WIDTH = 1440,
			REFLECTION_HEIGHT = 720,
			REFRACTION_HEIGHT = 500;

	private final FBO reflection;
	private final FBO refraction;
//	private FBO bluredReflection;
//	private FBO bluredRefraction;

	public WaterFBOs(boolean blured) {
		reflection = new FBO(REFLECTION_WIDTH, REFLECTION_HEIGHT, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		refraction = new FBO(REFRACTION_WIDTH, REFRACTION_HEIGHT, (FBO.COLOR_TEXTURE | FBO.DEPTH_TEXTURE)).create();
//		bluredReflection = new FBO(REFLECTION_WIDTH, REFLECTION_HEIGHT, (FBO.COLOR_TEXTURE)).create();
//		bluredRefraction = new FBO(REFRACTION_WIDTH, REFRACTION_HEIGHT, (FBO.COLOR_TEXTURE)).create();
	}

	public FBO getReflection() {
		return reflection;
	}

	public FBO getRefraction() {
		return refraction;
	}

	public void bindReflection() {
		reflection.bind();
	}

	public void bindRefraction() {
		refraction.bind();
	}

//	public void blur(Blurer blurer) {
//		blurer.apply(reflection, bluredReflection);
//		blurer.apply(refraction, bluredRefraction);
//	}
//
//	public FBO getBluredReflection() {
//		return bluredReflection;
//	}
//
//	public FBO getBluredRefraction() {
//		return bluredRefraction;
//	}
}