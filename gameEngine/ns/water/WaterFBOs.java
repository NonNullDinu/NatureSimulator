package ns.water;

import ns.openglObjects.FBO;

public class WaterFBOs {
	private FBO reflexion;
	private FBO refraction;
	
	public WaterFBOs() {
		reflexion = new FBO(1200, 800, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		refraction = new FBO(1200, 800, (FBO.COLOR_TEXTURE | FBO.DEPTH_TEXTURE)).create();
	}

	public FBO getReflexion() {
		return reflexion;
	}

	public FBO getRefraction() {
		return refraction;
	}
	
	public void bindReflexion() {
		reflexion.bind();
	}
	
	public void bindRefraction() {
		refraction.bind();
	}
	
	public void cleanUp() {
		reflexion.cleanUp();
		refraction.cleanUp();
	}
}