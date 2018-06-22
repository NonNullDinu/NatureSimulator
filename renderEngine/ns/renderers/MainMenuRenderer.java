package ns.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import ns.camera.ICamera;
import ns.entities.Entity;
import ns.mainMenu.MainMenu;
import ns.mainMenu.MainMenuButton;
import ns.openglObjects.FBO;
import ns.shaders.MenuDNAShader;
import ns.utils.Maths;

public class MainMenuRenderer {
	private GUIRenderer guiRenderer;
	private FBO DNAFBO;
	private MenuDNAShader shader;

	public MainMenuRenderer(GUIRenderer guiRenderer, ICamera camera) {
		this.guiRenderer = guiRenderer;
		DNAFBO = new FBO(600, 800, FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER).create();
		shader = new MenuDNAShader();
		shader.start();
		shader.projectionMatrix.load(camera.getProjectionMatrix());
		shader.stop();
	}

	public void render(MainMenu menu) {
		DNAFBO.bind();
		GL11.glClearColor(GUIRenderer.TRANSPARENCY.x, GUIRenderer.TRANSPARENCY.y, GUIRenderer.TRANSPARENCY.z, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		shader.start();
		Entity DNA = menu.getDNA();
		DNA.getModel().bind(0);
		shader.transformationMatrix.load(Maths.createTreansformationMatrix(DNA));
		DNA.getModel().batchRenderCall();
		DNA.getModel().unbind();
		shader.stop();
		FBO.unbind();
		guiRenderer.bind();
		guiRenderer.batchRenderCall(new Vector2f(-0.8f, 0.0f), new Vector2f(0.4f, 1f), DNAFBO.getTex());
		for (MainMenuButton button : menu.getButtons())
			guiRenderer.batchRenderCall(button.getCenter(), button.getScale(), button.getTex());
		guiRenderer.unbind();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}