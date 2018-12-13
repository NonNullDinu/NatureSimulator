package ns.mainEngine;

import ns.camera.ICamera;
import ns.customFileFormat.TexFile;
import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.entities.Moon;
import ns.entities.Sun;
import ns.flares.FlareManager;
import ns.fontRendering.TextMaster;
import ns.interfaces.Action;
import ns.mainMenu.MainMenu;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.options.Options;
import ns.parallelComputing.SetRequest;
import ns.parallelComputing.ThreadMaster;
import ns.renderers.*;
import ns.rivers.RiverList;
import ns.shaders.GUIShader;
import ns.shaders.MovingEntitySpotShader;
import ns.shaders.WaterShader;
import ns.ui.loading.UILoader;
import ns.ui.shop.Shop;
import ns.utils.GU;
import ns.utils.MousePicker;
import ns.water.WaterFBOs;
import ns.water.WaterTile;
import ns.world.World;
import ns.worldLoad.WorldLoadMaster;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import resources.Out;

import java.io.IOException;
import java.io.OutputStream;

class MainGameLoop implements Runnable {
	private OutputStream logOut;
	static GS state = GS.LOADING;

	private FBO bluredSceneFBO;
	private Blurer blurer;
	private ICamera camera;
	private WaterFBOs fbos;
	private MainMenu menu;
	private MainMenuRenderer menuRenderer;
	private MasterRenderer renderer;
	private FBO sceneFBO;
	private Shop shop;
	private ShopRenderer shopRenderer;
	private Sun sun;
	private Moon moon;

	private WaterTile water;
	private WaterRenderer waterRenderer;

	private World world;
	private Options options;
	private FlareManager flareManager;
	private RiverRenderer riverRenderer;
	private RiverList rivers;

	private Texture moonTex;

	private static void requestExecuteRequests() {
		((MainGameLoop) ThreadMaster.getThread(GU.MAIN_THREAD_NAME).getRunnable()).executeRequests();
	}

	private void executeRequests() {
		ns.parallelComputing.Thread thread = GU.currentThread();
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

	private void logic() {
		GU.rn_update();
		sun.update();
		moon.update(sun);
		flareManager.updateFlares(sun, camera);
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
		} else if (state == GS.MENU) {
			menu.update();
			if (GU.Key.KEY_ESC.pressedThisFrame()) {
				state = GS.GAME;
				if (shop.open())
					shop.refreshCursor();
			}
		} else if (state == GS.OPTIONS) {
			options.update();
		} else if (state == GS.LOAD_WORLD_SCREEN) {
			World wld = WorldLoadMaster.returnSelected();
			if (wld != null) {
				world = wld;
				state = GS.GAME;
				rivers = wld.getRivers();
				water.recreateModel(wld.getTerrain());
			}
		}
		GU.update();
	}

	private Vector2f convertToScreenSpace(Vector3f worldPos, Matrix4f viewMat, Matrix4f projectionMat) {
		Vector4f coords = new Vector4f(worldPos.x, worldPos.y, worldPos.z, 1f);
		Matrix4f.transform(viewMat, coords, coords);
		Matrix4f.transform(projectionMat, coords, coords);
		if (coords.w <= 0) {
			return null;
		}
		float x = (coords.x / coords.w + 1) / 2f;
		float y = 1 - ((coords.y / coords.w + 1) / 2f);
		return new Vector2f(x, y);
	}

	private void render() {
		GU.updateWireFrame();
		if (state == GS.GAME || state == GS.MENU || state == GS.OPTIONS || state == GS.LOAD_WORLD_SCREEN) {
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			fbos.bindReflection();
			float distance = 2 * camera.getPosition().y;
			camera.addToPositionNoViewMatUpdate(0, -distance, 0);
			camera.invertPitch();
			renderer.renderScene(world, camera, sun, moon, new Vector4f(0, 1, 0, 1f), true);
			riverRenderer.render(rivers, camera);
			fbos.bindRefraction();
			camera.addToPositionNoViewMatUpdate(0, distance, 0);
			camera.invertPitch();
			renderer.renderScene(world, camera, sun, moon, new Vector4f(0, -1, 0, 0.9f), false);
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			sceneFBO.bind();
			renderer.renderScene(world, camera, sun, moon, new Vector4f(0, 0, 0, 0), false);
			waterRenderer.render(water, camera, fbos, sun, moon);
			riverRenderer.render(rivers, camera);
			flareManager.render();

			Vector2f moonOnScreen = convertToScreenSpace(
					new Vector3f(camera.getPosition().x - moon.dir.x * 1000, camera.getPosition().y - moon.dir.y * 1000,
							camera.getPosition().z - moon.dir.z * 1000),
					camera.getViewMatrix(), camera.getProjectionMatrix());
			if (moonOnScreen != null) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				Vector2f moonToCenter = Vector2f.sub(FlareManager.CENTER, moonOnScreen, null);
				QuadRenderer.renderMaxDepth(new Vector2f(-moonToCenter.x, moonToCenter.y), new Vector2f(0.05f, 0.05f),
						moonTex, true);
				GL11.glDisable(GL11.GL_BLEND);
			}

			FBO.unbind();
			if (state == GS.GAME) {
				sceneFBO.blitToScreen();
				shopRenderer.render(shop);
			} else if (state == GS.MENU || state == GS.OPTIONS || state == GS.LOAD_WORLD_SCREEN) {
				MasterRenderer.prepare();
				blurer.apply(sceneFBO, bluredSceneFBO);
				bluredSceneFBO.blitToScreen();
				if (state == GS.MENU) {
					menuRenderer.render(menu);
				} else if (state == GS.OPTIONS) {
					options.render();
				} else if (state == GS.LOAD_WORLD_SCREEN) {
					WorldLoadMaster.renderUI();
				}
			}
		}
	}

	private void runLogicAndRender() {
		logic();
		render();
	}

	public void run() {
		GU.init();
		logOut = Out.create("GL_LOG").asOutputStream();
		SetRequest.init((Object... o) -> set(o[0]));
		VAO.init(MainGameLoop::requestExecuteRequests);
		UILoader.init(new Action[]{() -> MainGameLoop.state = GS.GAME, () -> MainGameLoop.state = GS.OPTIONS,
				() -> MainGameLoop.state = GS.EXIT, () -> MainGameLoop.state = GS.LOAD_WORLD_SCREEN,
				() -> MainGameLoop.state = GS.MENU});
		DisplayManager.createDisplay();
		GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
		GL43.glDebugMessageCallback(
				new KHRDebugCallback((int source, int type, int id, int severity, String message) -> {
					System.out.println(source + " " + type + " " + id + " " + severity + " " + message);
					try {
						logOut.write((GU.getKHR_DEBUG_CALLBACK_FIELD(source) + " "
								+ GU.getKHR_DEBUG_CALLBACK_FIELD(type) + " " + id + " "
								+ GU.getKHR_DEBUG_CALLBACK_FIELD(severity) + " " + message + "\n").getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
		TextMaster.init();
		executeRequests();
		WaterShader shader = new WaterShader();
		executeRequests();
		fbos = new WaterFBOs(true);
		while (camera == null)
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
		renderer.render(camera, sun, moon, new Vector4f(0, 0, 0, 0), false);
		executeRequests();
		GU.initMouseCursors(renderer);
		executeRequests();
		riverRenderer = new RiverRenderer(camera.getProjectionMatrix());
		MovingEntitySpotShader movingEntitySpotShader = new MovingEntitySpotShader();
		moonTex = new TexFile("textures/moon.tex").load();
		WorldLoadMaster.buildUI();
		GU.currentThread().finishLoading();
		state = GS.WAITING;
		while (!SecondaryThread.READY || !ThirdThread.READY || !LoadingScreenThread.READY) {
			executeRequests();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		executeRequests();
		state = GS.MENU;
		GU.rn_update();
		camera.update(world);
		while (!DisplayManager.isCloseRequested()) {
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
		guiShader.cleanUp();
		QuadRenderer.cleanUp();
		Texture.cleanUp();
		TextMaster.cleanUp();
		flareManager.cleanUp();
		riverRenderer.cleanUp();
		movingEntitySpotShader.cleanUp();
		FBO.clearUp();
		DisplayManager.closeDisplay();
		GU.currentThread().finishExecution();
	}

	private void set(Object o) {
		if (o instanceof Shop)
			this.shop = (Shop) o;
		else if (o instanceof World)
			this.world = (World) o;
		else if (o instanceof ICamera)
			this.camera = (ICamera) o;
		else if (o instanceof Sun)
			this.sun = (Sun) o;
		else if (o instanceof Moon)
			this.moon = (Moon) o;
		else if (o instanceof WaterTile)
			this.water = (WaterTile) o;
		else if (o instanceof MainMenu)
			this.menu = (MainMenu) o;
		else if (o instanceof Options)
			this.options = (Options) o;
		else if (o instanceof FlareManager)
			this.flareManager = (FlareManager) o;
		else if (o instanceof RiverList)
			this.rivers = (RiverList) o;
	}
}