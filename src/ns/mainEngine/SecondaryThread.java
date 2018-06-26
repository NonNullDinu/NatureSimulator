package ns.mainEngine;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.customFileFormat.TexFile;
import ns.derrivedOpenGLObjects.FlareTexture;
import ns.entities.Light;
import ns.flares.FlareManager;
import ns.mainMenu.MenuMaster;
import ns.openglObjects.Texture;
import ns.openglWorkers.ModelsLibrary;
import ns.options.OptionsMaster;
import ns.parallelComputing.SetRequest;
import ns.renderers.GUIRenderer;
import ns.renderers.MasterRenderer;
import ns.rivers.River;
import ns.rivers.RiverList;
import ns.shaders.ShaderLib;
import ns.shaders.StructLib;
import ns.terrain.Terrain;
import ns.ui.shop.ShopMaster;
import ns.utils.GU;
import ns.utils.GU.Random;
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
		ModelsLibrary.getModel("models/1000/tree.mdl");
		ModelsLibrary.getModel("models/1001/tree.mdl");
		ModelsLibrary.getModel("models/1002/tree.mdl");
		ModelsLibrary.getModel("models/1003/mushroom.mdl");
		ModelsLibrary.getModel("models/1004/tree.mdl");
		GU.sendRequestToMainThread(new SetRequest(
				new Light(new Vector3f(0.5f, -0.15f, 0), new Vector3f(1, 1, 1), new Vector2f(0.5f, 0.5f))));
		GU.sendRequestToMainThread(new SetRequest(OptionsMaster.createOptions()));

		// Read model files and create CreateVAORequests
		ModelsLibrary.getModel("models/others/menu_DNA.obj");
		GU.sendRequestToMainThread(new SetRequest(MenuMaster.createMainMenu()));

		while (GUIRenderer.instance == null)
			Thread.yield();

		GU.sendRequestToMainThread(new SetRequest(ShopMaster.createShop(GUIRenderer.instance)));

		Texture[] flareTextures = new Texture[] { new TexFile("textures/lensFlare/tex1.tex").load(),
				new TexFile("textures/lensFlare/tex2.tex").load(), new TexFile("textures/lensFlare/tex3.tex").load(),
				new TexFile("textures/lensFlare/tex4.tex").load(), new TexFile("textures/lensFlare/tex5.tex").load(),
				new TexFile("textures/lensFlare/tex6.tex").load(), new TexFile("textures/lensFlare/tex7.tex").load(),
				new TexFile("textures/lensFlare/tex8.tex").load(), };
		GU.sendRequestToMainThread(new SetRequest(new FlareManager(new TexFile("textures/lensFlare/sun.tex").load(),
				new FlareTexture(flareTextures[7], 0.5f, 35f), new FlareTexture(flareTextures[5], 0.2f),
				new FlareTexture(flareTextures[1], 0.2f), new FlareTexture(flareTextures[2], 0.1f),
				new FlareTexture(flareTextures[0], 0.1f), new FlareTexture(flareTextures[5], 0.2f),
				new FlareTexture(flareTextures[6], 0.2f), new FlareTexture(flareTextures[3], 0.3f))));

		while (WorldGenerator.generatedWorld == null)
			Thread.yield();
		GU.sendRequestToMainThread(new SetRequest(new WaterTile(0, 0, WorldGenerator.generatedWorld.getTerrain())));
		if (WorldGenerator.generatedWorld.getRivers() == null) {
			RiverList riverList = new RiverList();
			Random random = GU.random;
			Terrain terrain = WorldGenerator.generatedWorld.getTerrain();
			final float TS = Terrain.SIZE / 2f;
			for (int i = 0; i < 10; i++) {
				float x = random.genFloat();
				float z = random.genFloat();
				float y = terrain.getHeight(x, z);
				while (y < 50f) {
					x = (random.genFloat() * 2.0f - 1.0f) * TS;
					z = (random.genFloat() * 2.0f - 1.0f) * TS;
					y = terrain.getHeight(x, z);
				}
				riverList.add(new River(new Vector3f(x, y, z)));
			}
			WorldGenerator.generatedWorld.setRivers(riverList);
			GU.sendRequestToMainThread(new SetRequest(riverList));
		}

		Runtime.getRuntime().gc();
		READY = true;
		GU.currentThread().finishLoading();
	}
}