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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ThirdThread implements Runnable {
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

		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(">");
		while (MainGameLoop.state != GS.CLOSING) {
			try {
				if (cin.ready()) {
					String ln = cin.readLine();
					execute(ln);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Thread.yield();
		}

		SaveWorldMaster.save(world,
				SaveData.openOutput("save0." + GU.WORLD_SAVE_FILE_FORMAT));
		GU.currentThread().finishExecution();
	}

	private void execute(String ln) {
		if (ln.equals("show-time")) {
			System.out.print("Simulation seconds since begun: " + GU.time.t + "; days passed in simulation " + GU.time.day() + "\n>");
		}
	}
}