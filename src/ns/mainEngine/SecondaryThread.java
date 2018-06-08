package ns.mainEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.camera.Camera;
import ns.display.DisplayManager;
import ns.entities.Light;
import ns.openglWorkers.ModelsLibrary;
import ns.renderers.GUIRenderer;
import ns.renderers.MasterRenderer;
import ns.shaders.ShaderLib;
import ns.shaders.StructLib;
import ns.ui.shop.ShopMaster;
import ns.utils.GU;
import ns.water.WaterTile;
import ns.world.World;
import ns.world.WorldGenerator;
import ns.worldSave.SaveWorldMaster;
import res.Resource;
import res.WritingResource;

public class SecondaryThread implements Runnable {
	@Override
	public void run() {
		long timeb = System.nanoTime();
		while(DisplayManager.drawable == null)
			Thread.yield();
		try {
			DisplayManager.drawable.makeCurrent();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		MasterRenderer.initStandardModels();
		StructLib.load(new Resource().withLocation("shaders/structlib.glsl").withVersion(false).create());
		ShaderLib.loadAll();
		new Camera();
		new Light(new Vector3f(0.5f, -0.5f, 0), new Vector3f(1, 1, 1), new Vector2f(0.5f, 0.5f));
		World world = WorldGenerator.generateWorld();
		new WaterTile(0, 0);

		// Read model files and create CreateVAORequests
		ModelsLibrary.getModel("models/others/menu_DNA.obj");
		while (GUIRenderer.instance == null)
			Thread.yield();

		ShopMaster.createShop(GUIRenderer.instance);

		Runtime.getRuntime().gc();
		System.out.println("Secondary thread finished in " + (System.nanoTime() - timeb));
		while (MainGameLoop.inLoop) {
			Thread.yield();
		}

		SaveWorldMaster.save(world, new WritingResource("saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT));
	}
}