package ns.mainEngine;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.entities.Light;
import ns.mainMenu.MenuMaster;
import ns.openglWorkers.ModelsLibrary;
import ns.options.OptionsMaster;
import ns.parallelComputing.SetRequest;
import ns.renderers.GUIRenderer;
import ns.renderers.MasterRenderer;
import ns.shaders.ShaderLib;
import ns.shaders.StructLib;
import ns.ui.shop.ShopMaster;
import ns.utils.GU;
import ns.water.WaterTile;
import ns.world.WorldGenerator;
import res.Resource;

public class SecondaryThread implements Runnable {
	public static boolean READY = false;

	@Override
	public void run() {
		MasterRenderer.initStandardModels();
		StructLib.load(new Resource().withLocation("shaders/structlib.glsl").withVersion(false).create());
		ShaderLib.loadAll();
		ModelsLibrary.getModel("models/1000/tree.obj");
		ModelsLibrary.getModel("models/1001/tree.obj");
		ModelsLibrary.getModel("models/1002/tree.obj");
		ModelsLibrary.getModel("models/1003/mushroom.obj");
		ModelsLibrary.getModel("models/1004/tree.obj");
		GU.sendRequestToMainThread(new SetRequest(
				new Light(new Vector3f(0.5f, -0.5f, 0), new Vector3f(1, 1, 1), new Vector2f(0.5f, 0.5f))));
		GU.sendRequestToMainThread(new SetRequest(OptionsMaster.createOptions()));

		// Read model files and create CreateVAORequests
		ModelsLibrary.getModel("models/others/menu_DNA.obj");
		GU.sendRequestToMainThread(new SetRequest(MenuMaster.createMainMenu()));

		while (GUIRenderer.instance == null)
			Thread.yield();

		GU.sendRequestToMainThread(new SetRequest(ShopMaster.createShop(GUIRenderer.instance)));

		while (WorldGenerator.generatedWorld == null)
			Thread.yield();
		GU.sendRequestToMainThread(new SetRequest(new WaterTile(0, 0, WorldGenerator.generatedWorld.getTerrain())));

		Runtime.getRuntime().gc();
		READY = true;
		GU.currentThread().finishLoading();
	}
}