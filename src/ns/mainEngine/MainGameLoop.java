package ns.mainEngine;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import ns.parallelComputing.Request;
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
import ns.worldSave.SaveWorldMaster;
import res.WritingResource;

/**
 * @version 1.1.8
 */
public class MainGameLoop implements Runnable {
	public static GS state = GS.LOADING;
	public static int leaves = 10000;

	public static void main(String[] args) {
		UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				String msg = "";
				for (StackTraceElement elem : e.getStackTrace()) {
					// Formatting for use with eclipse
					msg += elem.getModuleName() + "/" + elem.getClassName() + "." + elem.getMethodName() + "("
							+ elem.getFileName() + ":" + elem.getLineNumber() + ")\n	";
				}
				File f = new File("err" + new SimpleDateFormat("hh mm ss dd MM yyyy").format(new Date()) + ".log");
				System.err.println(e.getClass().getName() + "\nStack trace: " + msg);
				try {
					f.createNewFile();
					DataOutputStream dout = new DataOutputStream(new FileOutputStream(f));
					dout.writeUTF(e.getClass().getName() + "\nStack trace: " + msg);
					dout.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					SaveWorldMaster.save(WorldGenerator.generatedWorld,
							new WritingResource("saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT));
				} catch (Throwable thr) {
					msg = "";
					for (StackTraceElement elem : thr.getStackTrace()) {
						msg += elem.getFileName() + ": " + elem.getMethodName() + "(line " + elem.getLineNumber()
								+ ")\n	";
					}
					System.err.print(thr.getClass().getName() + "\nError while saving world:\nAt:" + msg);
					new File("saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT).delete();
				}
				System.exit(-1);
			}
		};

		ns.parallelComputing.Thread thread;
		thread = ThreadMaster.createThread(new MainGameLoop(), "main thread");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new SecondaryThread(), "secondary thread");
		thread.setUncaughtExceptionHandler(handler);
		thread.start();

		thread = ThreadMaster.createThread(new LoadingScreenThread(), "loading screen thread");
		thread.setUncaughtExceptionHandler(handler);
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
		synchronized (thread.vaoCreateRequests) {
			for (int i = 0; i < thread.vaoCreateRequests.size(); i++)
				thread.vaoCreateRequests.get(i).execute();
		}
		synchronized (thread.renderingRequests) {
			for (int i = 0; i < thread.renderingRequests.size(); i++) {
				Request r = thread.renderingRequests.get(i);
				r.execute();
			}
		}
		synchronized (thread.toCarryOutRequests) {
			for (int i = 0; i < thread.toCarryOutRequests.size(); i++) {
				Request r = thread.toCarryOutRequests.get(i);
				r.execute();
			}
		}
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
		DisplayManager.createDisplay();
		TextMaster.init();
		renderer = new MasterRenderer();
		executeRequests();
		DisplayManager.updateDisplay();
		shader = new WaterShader();
		waterRenderer = new WaterRenderer(shader, renderer.getProjectionMatrix());
		executeRequests();
		fbos = new WaterFBOs();
		camera = ICamera.createdCamera;
		sun = Light.sun;
		executeRequests();
		sceneFBO = new FBO(1200, 800, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
		bluredSceneFBO = new FBO(Display.getWidth() / 3, Display.getHeight() / 3, FBO.COLOR_TEXTURE).create();
		blurer = new Blurer(MasterRenderer.standardModels.get(0));
		executeRequests();
		GUIShader guiShader = new GUIShader();
		GUIRenderer guiRenderer = new GUIRenderer(guiShader, MasterRenderer.standardModels.get(0));
		menu = MenuMaster.createMainMenu();
		executeRequests();
		menuRenderer = new MainMenuRenderer(guiRenderer);
		executeRequests();
		shopRenderer = new ShopRenderer(guiRenderer);
		executeRequests();
		while (ShopMaster.shop == null)
			Thread.yield();
		shop = ShopMaster.shop;
		ColorQuadFiller.init();
		renderer.render(camera, sun, new Vector4f(0, 0, 0, 0), false);
		executeRequests();
		GU.initMouseCursors(renderer);
		executeRequests();
		world = WorldGenerator.generatedWorld;
		MousePicker.init(camera, renderer.getProjectionMatrix(), world.getTerrain());
		water = WaterTile.tile;
		executeRequests();
		state = GS.MENU;
		while (!Display.isCloseRequested()) {
			if (state == GS.EXIT)
				break;
			runLogicAndRender();
			DisplayManager.updateDisplay();
			executeRequests();
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
}