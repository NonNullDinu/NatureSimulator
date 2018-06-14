package ns.mainEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

import ns.camera.ICamera;
import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.entities.Light;
import ns.fontRendering.TextMaster;
import ns.mainMenu.MainMenu;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.openglWorkers.VAOLoader;
import ns.options.Options;
import ns.renderers.Blurer;
import ns.renderers.ColorQuadFiller;
import ns.renderers.GUIRenderer;
import ns.renderers.MainMenuRenderer;
import ns.renderers.MasterRenderer;
import ns.renderers.ShopRenderer;
import ns.renderers.WaterRenderer;
import ns.shaders.GUIShader;
import ns.shaders.WaterShader;
import ns.ui.shop.Shop;
import ns.utils.GU;
import ns.utils.MousePicker;
import ns.water.WaterFBOs;
import ns.water.WaterTile;
import ns.world.World;

public class MainGameLoop implements Runnable {
	public static GS state = GS.LOADING;
	public static int leaves = 10000;

	private FBO bluredSceneFBO;
	private Blurer blurer;
	private ICamera camera;
	private WaterFBOs fbos;
	private MainMenu menu;
	private MainMenuRenderer menuRenderer;
	private MasterRenderer renderer;
	private FBO sceneFBO;
	private WaterShader shader;
	private Shop shop;
	private ShopRenderer shopRenderer;
	private Light sun;

	private WaterTile water;
	private WaterRenderer waterRenderer;

	private World world;
	private Options options;

	public void executeRequests() {
		ns.parallelComputing.Thread thread = GU.currentThread();
		synchronized (thread) {
			thread.isExecutingRequests = true;
			for (int i = 0; i < thread.vaoCreateRequests.size(); i++)
				thread.vaoCreateRequests.get(i).execute();
			for (int i = 0; i < thread.toCarryOutRequests.size(); i++)
				thread.toCarryOutRequests.get(i).execute();
			for (int i = 0; i < thread.renderingRequests.size(); i++)
				thread.renderingRequests.get(i).execute();
			thread.clearRequests();
			thread.isExecutingRequests = false;
		}
	}

	public void logic() {
		if (state == GS.GAME) {
			MousePicker.update();
			Entity e = shop.update();
			if (e != null)
				world.add(e);
			camera.update(world);
			world.update();
			if (GU.Key.KEY_ESC.pressedThisFrame()) {
				state = GS.MENU;
				if (shop.open())
					try {
						Mouse.setNativeCursor(null);
					} catch (LWJGLException e1) {
						e1.printStackTrace();
					}
			}
		} else if (state == GS.MENU) {
			menu.update();
			if (GU.Key.KEY_ESC.pressedThisFrame()) {
				state = GS.GAME;
				if (shop.open())
					shop.refreshCursor();
			}
		} else if (state == GS.OPTIONS) {
			options.update();
		}
		GU.update();
		GU.updateWireFrame();
	}

	public void render() {
		if (state == GS.GAME || state == GS.MENU || state == GS.OPTIONS) {
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			fbos.bindReflexion();
			float distance = 2 * camera.getPosition().y;
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(world, camera, sun, new Vector4f(0, 1, 0, 0f), true);
			fbos.bindRefraction();
			camera.getPosition().y += distance;
			camera.invertPitch();
			renderer.renderScene(world, camera, sun, new Vector4f(0, -1, 0, 2.0f), false);
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			if (state == GS.GAME) {
				FBO.unbind();
				MasterRenderer.prepare();
				renderer.renderScene(world, camera, sun, new Vector4f(0, 0, 0, 0), false);
				waterRenderer.render(water, camera, fbos, sun);
				shopRenderer.render(shop);
			} else if (state == GS.MENU || state == GS.OPTIONS) {
				sceneFBO.bind();
				renderer.renderScene(world, camera, sun, new Vector4f(0, 0, 0, 0), false);
				waterRenderer.render(water, camera, fbos, sun);
				FBO.unbind();
				MasterRenderer.prepare();
				blurer.apply(sceneFBO, bluredSceneFBO);
				bluredSceneFBO.blitToScreen();
				if (state == GS.MENU) {
					menuRenderer.render(menu);
				} else if (state == GS.OPTIONS) {
					options.render();
				}
			}
		}
	}

	public void run() {
		DisplayManager.createDisplay();
		renderer = new MasterRenderer();
		TextMaster.init();
		executeRequests();
		shader = new WaterShader();
		waterRenderer = new WaterRenderer(shader, renderer.getProjectionMatrix());
		executeRequests();
		fbos = new WaterFBOs();
		executeRequests();
		sceneFBO = new FBO(1200, 800, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		bluredSceneFBO = new FBO(Display.getWidth() / 4, Display.getHeight() / 4, FBO.COLOR_TEXTURE).create();
		blurer = new Blurer(MasterRenderer.standardModels.get(0));
		executeRequests();
		GUIShader guiShader = new GUIShader();
		GUIRenderer guiRenderer = new GUIRenderer(guiShader, MasterRenderer.standardModels.get(0));
		executeRequests();
		menuRenderer = new MainMenuRenderer(guiRenderer);
		executeRequests();
		shopRenderer = new ShopRenderer(guiRenderer);
		executeRequests();
		ColorQuadFiller.init();
		renderer.render(camera, sun, new Vector4f(0, 0, 0, 0), false);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executeRequests();
		GU.initMouseCursors(renderer);
		executeRequests();
		state = GS.MENU;
		GU.currentThread().finishLoading();
		while (!SecondaryThread.READY || !ThirdThread.READY) {
			executeRequests();
			Thread.yield();
		}
		while (!Display.isCloseRequested()) {
			if (state == GS.EXIT)
				break;
			runLogicAndRender();
			DisplayManager.updateDisplay();
			executeRequests();
			assert (GL11.glGetError() == GL11.GL_NO_ERROR);
		}
		state = GS.CLOSING;
		VAOLoader.cleanUp();
		renderer.cleanUp();
		menuRenderer.cleanUp();
		shader.cleanUp();
		blurer.cleanUp();
		shopRenderer.cleanUp();
		sceneFBO.cleanUp();
		bluredSceneFBO.cleanUp();
		guiShader.cleanUp();
		ColorQuadFiller.cleanUp();
		Texture.cleanUp();
		TextMaster.cleanUp();
		DisplayManager.closeDisplay();
	}

	private void runLogicAndRender() {
		logic();
		render();
	}

	public void set(Object o) {
		if (o instanceof Shop)
			this.shop = (Shop) o;
		if (o instanceof World)
			this.world = (World) o;
		if (o instanceof ICamera)
			this.camera = (ICamera) o;
		if (o instanceof Light)
			this.sun = (Light) o;
		if (o instanceof WaterTile)
			this.water = (WaterTile) o;
		if (o instanceof MainMenu)
			this.menu = (MainMenu) o;
		if (o instanceof Options)
			this.options = (Options) o;
	}
}