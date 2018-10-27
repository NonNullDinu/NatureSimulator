package ns.mainEngine;

import data.SaveData;
import ns.camera.Camera;
import ns.camera.ICamera;
import ns.display.DisplayManager;
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
			String ln = null;
			try {
				if (cin.ready()) {
					ln = cin.readLine();
					execute(ln);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				System.err.println("Number format exception \"" + e.getMessage() + "\n at line: " + ln);
			}
			Thread.yield();
		}

		SaveWorldMaster.save(world, SaveData.openOutput("save0." + GU.WORLD_SAVE_FILE_FORMAT));
		GU.currentThread().finishExecution();
	}

	private void execute(String ln) throws NumberFormatException {
		if (ln.equals("show-time")) {
			System.out.print("Simulation seconds since begun: " + GU.time.t + "; days passed in simulation " + GU.time.day() + "\n>");
		} else if (ln.equals("h") || ln.equals("help")) {
			System.out.print("Commands available:\n\tshow-time --- shows the time since the begin of the " +
					"simulation\n\th[elp] --- shows this list\n>");
		} else if (ln.startsWith("time-rate ")) {
			float arg = Float.parseFloat(ln.substring(10));
			DisplayManager.time_rate(arg);
			System.out.print("In-game time-rate changed to " + arg + "\n>");
		} else if (ln.startsWith("time-rate-add ")) {
			float arg = Float.parseFloat(ln.substring(14));
			arg += DisplayManager.time_rate * 1000f;
			DisplayManager.time_rate(arg);
			System.out.print("In-game time-rate changed to " + arg + "\n>");
		}
	}
}