/*
 * Copyright (C) 2018  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.mainEngine;

import data.GameData;
import ns.customFileFormat.TexFile;
import ns.derrivedOpenGLObjects.FlareTexture;
import ns.entities.Moon;
import ns.entities.Sun;
import ns.flares.FlareManager;
import ns.mainMenu.MainMenu;
import ns.mainMenu.MenuMaster;
import ns.openglObjects.Texture;
import ns.openglWorkers.ModelsLibrary;
import ns.options.Options;
import ns.options.OptionsMaster;
import ns.parallelComputing.SetRequest;
import ns.renderers.GUIRenderer;
import ns.renderers.MasterRenderer;
import ns.rivers.River;
import ns.rivers.RiverList;
import ns.shaders.ShaderLib;
import ns.shaders.StructLib;
import ns.terrain.Terrain;
import ns.ui.shop.Shop;
import ns.ui.shop.ShopMaster;
import ns.utils.GU;
import ns.utils.GU.Random;
import ns.water.WaterTile;
import ns.world.WorldGenerator;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

class SecondaryThread implements Runnable {
	public static boolean READY = false;

	@Override
	public void run() {
		MasterRenderer.initStandardModels();
		GU.currentThread().waitForGameDataInit();
		StructLib.load(GameData.getResourceAt("shaders/structlib.glsl"));
		GU.currentThread().waitForDisplayInit();
		ShaderLib.loadAll();
		ModelsLibrary.getModel("models/1000/tree.mdl");
		ModelsLibrary.getModel("models/1001/tree.mdl");
		ModelsLibrary.getModel("models/1002/tree.mdl");
		ModelsLibrary.getModel("models/1003/mushroom.mdl");
		ModelsLibrary.getModel("models/1004/tree.mdl");
		Sun sun = new Sun(new Vector3f(0.5f, 0f, 0), new Vector3f(1, 1, 1), new Vector2f(0.2f, 0.4f));
		GU.sendRequestToMainThread(new SetRequest(sun));
		Moon moon = new Moon(new Vector3f(0.5f, 0f, 0), new Vector3f(0.5f, 0.5f, 0.5f), new Vector2f(0.1f, 0.5f));
		GU.sendRequestToMainThread(new SetRequest(moon));
		Options options = OptionsMaster.createOptions();
		GU.sendRequestToMainThread(new SetRequest(options));

		// Read model files and create CreateVAORequests
		ModelsLibrary.getModel("models/others/menu_DNA.obj");
		MainMenu menu = MenuMaster.createMainMenu();
		GU.sendRequestToMainThread(new SetRequest(menu));

		while (GUIRenderer.instance == null)
			Thread.yield();

		Shop shop = ShopMaster.createShop(GUIRenderer.instance);
		GU.sendRequestToMainThread(new SetRequest(shop));

		Texture[] flareTextures = new Texture[] { new TexFile("textures/lensFlare/tex1.tex").load(),
				new TexFile("textures/lensFlare/tex2.tex").load(), new TexFile("textures/lensFlare/tex3.tex").load(),
				new TexFile("textures/lensFlare/tex4.tex").load(), new TexFile("textures/lensFlare/tex5.tex").load(),
				new TexFile("textures/lensFlare/tex6.tex").load(), new TexFile("textures/lensFlare/tex7.tex").load(),
				new TexFile("textures/lensFlare/tex8.tex").load()};
		FlareManager flareManager = new FlareManager(new TexFile("textures/lensFlare/sun.tex").load(),
				new FlareTexture(flareTextures[7], 0.5f, 35f), new FlareTexture(flareTextures[5], 0.2f),
				new FlareTexture(flareTextures[1], 0.2f), new FlareTexture(flareTextures[2], 0.1f),
				new FlareTexture(flareTextures[0], 0.1f), new FlareTexture(flareTextures[5], 0.2f),
				new FlareTexture(flareTextures[6], 0.2f), new FlareTexture(flareTextures[3], 0.3f));
		GU.sendRequestToMainThread(new SetRequest(flareManager));

		while (WorldGenerator.generatedWorld == null)
			Thread.yield();
		GU.sendRequestToMainThread(new SetRequest(new WaterTile(0, 0, WorldGenerator.generatedWorld.getTerrain())));
		RiverList riverList = WorldGenerator.generatedWorld.getRivers();
		if (riverList == null) {
			riverList = new RiverList();
			Random random = GU.random;
			Terrain terrain = WorldGenerator.generatedWorld.getTerrain();
			final float TS = Terrain.SIZE / 2f;
			for (int i = 0; i < 50; i++) {
				float x = random.genFloat();
				float z = random.genFloat();
				float y = terrain.getHeight(x, z);
				while (y < 100f) {
					x = (random.genFloat() * 2.0f - 1.0f) * TS;
					z = (random.genFloat() * 2.0f - 1.0f) * TS;
					y = terrain.getHeight(x, z);
				}
				riverList.add(new River(new Vector3f(x, y, z)));
			}
			WorldGenerator.generatedWorld.setRivers(riverList);
		}
		GU.sendRequestToMainThread(new SetRequest(riverList));

//		Runtime.getRuntime().gc();
		READY = true;
		GU.currentThread().finishLoading();
		GU.currentThread().finishExecution();
	}
}