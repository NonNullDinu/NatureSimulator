package ns.mainEngine;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

import ns.camera.ICamera;
import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.entities.Light;
import ns.mainMenu.MainMenu;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.openglWorkers.VAOLoader;
import ns.parallelComputing.ThreadMaster;
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
import ns.ui.shop.ShopMaster;
import ns.utils.GU;
import ns.utils.MousePicker;
import ns.water.WaterFBOs;
import ns.water.WaterTile;
import ns.world.World;
import ns.world.WorldGenerator;

/**
 * @version 1.1.6
 * @author Dinu B.
 * @since 1.0
 */
public class MainGameLoop implements Runnable {
	protected static boolean inLoop = true;
	public static boolean loopStarted = false;
	public static GS state = GS.MENU;

	public static void main(String[] args) {
		ns.parallelComputing.Thread thread = ThreadMaster.createThread(new MainGameLoop(), "main thread");
		thread.start();
		thread = ThreadMaster.createThread(new SecondaryThread(), "secondary thread");
		thread.start();
	}

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

	public void executeRequests() {
		ns.parallelComputing.Thread thread = (ns.parallelComputing.Thread) java.lang.Thread.currentThread();
		for (int i = 0; i < thread.vaoCreateRequests.size(); i++)
			thread.vaoCreateRequests.get(i).execute();
		for (int i = 0; i < thread.toCarryOutRequests.size(); i++)
			thread.toCarryOutRequests.get(i).execute();
		thread.clearRequests();
	}

	public void logic() {
		if (state == GS.GAME) {
			MousePicker.update();
			Entity e = shop.update();
			if (e != null)
				world.add(e);
			camera.update(world);
			world.update();
		} else if (state == GS.MENU) {
			menu.update();
		}
		GU.update();
		GU.updateWireFrame();
	}

	public void render() {
		if (state == GS.GAME || state == GS.MENU) {
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
			} else if (state == GS.MENU) {
				sceneFBO.bind();
				renderer.renderScene(world, camera, sun, new Vector4f(0, 0, 0, 0), false);
				waterRenderer.render(water, camera, fbos, sun);
				FBO.unbind();
				MasterRenderer.prepare();
				blurer.apply(sceneFBO, bluredSceneFBO);
				bluredSceneFBO.blitToScreen();
				menuRenderer.render(menu);
			}
		}
	}

	public void run() {
		long btime = System.nanoTime();
		DisplayManager.createDisplay();
		renderer = new MasterRenderer();
		shader = new WaterShader();
		waterRenderer = new WaterRenderer(shader, renderer.getProjectionMatrix());
		fbos = new WaterFBOs();
		camera = ICamera.createdCamera;
		sun = Light.sun;
		while (WorldGenerator.generatedWorld == null)
			java.lang.Thread.yield();
		world = WorldGenerator.generatedWorld;
		while (WaterTile.tile == null)
			java.lang.Thread.yield();
		water = WaterTile.tile;
		executeRequests();
		sceneFBO = new FBO(1200, 800, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		bluredSceneFBO = new FBO(Display.getWidth() / 3, Display.getHeight() / 3, FBO.COLOR_TEXTURE).create();
		blurer = new Blurer(MasterRenderer.standardModels.get(0));
		GUIShader guiShader = new GUIShader();
		GUIRenderer guiRenderer = new GUIRenderer(guiShader, MasterRenderer.standardModels.get(0));
		menu = MenuMaster.createMainMenu();
		menuRenderer = new MainMenuRenderer(guiRenderer);
		shopRenderer = new ShopRenderer(guiRenderer);
		while (ShopMaster.shop == null)
			Thread.yield();
		shop = ShopMaster.shop;
		MousePicker.init(camera, renderer.getProjectionMatrix(), world.getTerrain());
		ColorQuadFiller.init();
		renderer.render(camera, sun, new Vector4f(0, 0, 0, 0), false);
		executeRequests();
		GU.initMouseCursors(renderer);
		System.out.println("Primary thread finished in " + (System.nanoTime() - btime));
		loopStarted = true;
		while (!Display.isCloseRequested()) {
			if (state == GS.EXIT)
				break;
			runLogicAndRender();
			DisplayManager.updateDisplay();
			executeRequests();
		}
		inLoop = false;
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
		DisplayManager.closeDisplay();
	}

	private void runLogicAndRender() {
		logic();
		render();
	}
}