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
import ns.flares.FlareManager;
import ns.fontRendering.TextMaster;
import ns.mainMenu.MainMenu;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.openglWorkers.VAOLoader;
import ns.options.Options;
import ns.parallelComputing.ThreadMaster;
import ns.renderers.Blurer;
import ns.renderers.GUIRenderer;
import ns.renderers.MainMenuRenderer;
import ns.renderers.MasterRenderer;
import ns.renderers.QuadRenderer;
import ns.renderers.RiverRenderer;
import ns.renderers.ShopRenderer;
import ns.renderers.WaterRenderer;
import ns.rivers.RiverList;
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
	private FlareManager flareManager;
	private RiverRenderer riverRenderer;
	private RiverList rivers;

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
			if (e != null) {
				e.rotate(0, GU.random.genFloat() * 360f, 0);
				world.add(e);
			}
			camera.update(world);
			world.update();
			rivers.update(world);
			if (GU.Key.KEY_ESC.pressedThisFrame()) {
				state = GS.MENU;
				if (shop.open())
					try {
						Mouse.setNativeCursor(null);
					} catch (LWJGLException e1) {
						e1.printStackTrace();
					}
			}
			flareManager.updateFlares(sun, camera);
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
			fbos.bindReflection();
			float distance = 2 * camera.getPosition().y;
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(world, camera, sun, new Vector4f(0, 1, 0, 0.6f), true);
			fbos.bindRefraction();
			camera.getPosition().y += distance;
			camera.invertPitch();
			renderer.renderScene(world, camera, sun, new Vector4f(0, -1, 0, 0.8f), false);
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			sceneFBO.bind();
			renderer.renderScene(world, camera, sun, new Vector4f(0, 0, 0, 0), false);
			fbos.blur(blurer);
			sceneFBO.bind();
			waterRenderer.renderBlured(water, camera, fbos, sun);
			riverRenderer.render(rivers, camera);
			flareManager.render();
			FBO.unbind();
			if (state == GS.GAME) {
				sceneFBO.blitToScreen();
				shopRenderer.render(shop);
			} else if (state == GS.MENU || state == GS.OPTIONS) {
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
		GU.init();
		DisplayManager.createDisplay();
		TextMaster.init();
		executeRequests();
		shader = new WaterShader();
		executeRequests();
		fbos = new WaterFBOs(true);
		while(camera == null)
			executeRequests();
		renderer = new MasterRenderer(camera);
		executeRequests();
		sceneFBO = new FBO(1200, 800, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		bluredSceneFBO = new FBO(Display.getWidth() / 4, Display.getHeight() / 4, FBO.COLOR_TEXTURE).create();
		blurer = new Blurer(MasterRenderer.standardModels.get(0));
		executeRequests();
		GUIShader guiShader = new GUIShader();
		GUIRenderer guiRenderer = new GUIRenderer(guiShader, MasterRenderer.standardModels.get(0));
		executeRequests();
		waterRenderer = new WaterRenderer(shader, camera.getProjectionMatrix());
		menuRenderer = new MainMenuRenderer(guiRenderer, camera);
		executeRequests();
		shopRenderer = new ShopRenderer(guiRenderer);
		executeRequests();
		QuadRenderer.init();
		renderer.render(camera, sun, new Vector4f(0, 0, 0, 0), false);
		executeRequests();
		GU.initMouseCursors(renderer);
		executeRequests();
		state = GS.MENU;
		riverRenderer = new RiverRenderer(camera.getProjectionMatrix());
		GU.currentThread().finishLoading();
		while (!SecondaryThread.READY || !ThirdThread.READY) {
			executeRequests();
			Thread.yield();
		}
		executeRequests();
		while (!Display.isCloseRequested()) {
			if (state == GS.EXIT)
				break;
			runLogicAndRender();
			DisplayManager.updateDisplay();
			executeRequests();
			int err = GL11.glGetError();
			if (err != GL11.GL_NO_ERROR)
				System.err.println("GL error " + err + "(" + GU.getGLErrorType(err) + ")");
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
		QuadRenderer.cleanUp();
		Texture.cleanUp();
		TextMaster.cleanUp();
		flareManager.cleanUp();
		riverRenderer.cleanUp();
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
		if (o instanceof FlareManager)
			this.flareManager = (FlareManager) o;
		if (o instanceof RiverList)
			this.rivers = (RiverList) o;
	}

	public static void requestExecuteRequests() {
		((MainGameLoop) ThreadMaster.getThread(GU.MAIN_THREAD_NAME).getRunnable()).executeRequests();
	}
}