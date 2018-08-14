package ns.mainEngine;

import data.SaveData;
import ns.camera.Camera;
import ns.camera.ICamera;
import ns.parallelComputing.SetRequest;
import ns.renderers.MasterRenderer;
import ns.utils.GU;
import ns.utils.MousePicker;
import ns.world.World;
import ns.world.WorldGenerator;
import ns.worldSave.SaveWorldMaster;

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
		GU.currentThread()._stop(() -> MainGameLoop.state != GS.CLOSING);

		SaveWorldMaster.save(world,
				SaveData.openOutput("save0." + GU.WORLD_SAVE_FILE_FORMAT));
		GU.currentThread().checkpoint();
		GU.currentThread().finishExecution();
	}
}