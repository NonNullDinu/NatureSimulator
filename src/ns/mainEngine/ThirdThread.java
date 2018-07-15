package ns.mainEngine;

import ns.camera.Camera;
import ns.camera.ICamera;
import ns.parallelComputing.SetRequest;
import ns.renderers.MasterRenderer;
import ns.utils.GU;
import ns.utils.MousePicker;
import ns.world.World;
import ns.world.WorldGenerator;
import ns.worldSave.SaveWorldMaster;
import resources.WritingResource;

public class ThirdThread implements Runnable {
	public static boolean READY = false;

	@Override
	public void run() {
		GU.currentThread().waitForDisplayInit();
		ICamera camera = new Camera(GU.creteProjection(70, 0.1f, MasterRenderer.FAR_PLANE));
		GU.sendRequestToMainThread(new SetRequest(camera));
		World world = WorldGenerator.generateWorld();
		GU.sendRequestToMainThread(new SetRequest(world));
		while (MasterRenderer.instance == null)
			Thread.yield();
		MousePicker.init(camera, world.getTerrain());
		if (world.getRivers() != null)
			world.getRivers().update(world);

		GU.currentThread().finishLoading();
		READY = true;
		while (MainGameLoop.state != GS.CLOSING) {
			Thread.yield();
		}

		SaveWorldMaster.save(world,
				new WritingResource().withLocation(GU.path + "saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT).create());
	}
}