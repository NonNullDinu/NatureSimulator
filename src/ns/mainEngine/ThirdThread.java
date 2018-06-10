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
import res.WritingResource;

public class ThirdThread implements Runnable {
	public static boolean READY = false;

	@Override
	public void run() {
		ICamera camera = new Camera();
		GU.sendRequestToMainThread(new SetRequest(camera));
		World world = WorldGenerator.generateWorld();
		GU.sendRequestToMainThread(new SetRequest(world));
		MousePicker.init(camera, MasterRenderer.instance.getProjectionMatrix(), world.getTerrain());
		
		GU.currentThread().finishLoading();
		READY = true;
		while (MainGameLoop.state != GS.CLOSING) {
			Thread.yield();
		}

		SaveWorldMaster.save(world, new WritingResource("saveData/save0." + GU.WORLD_SAVE_FILE_FORMAT));
	}
}