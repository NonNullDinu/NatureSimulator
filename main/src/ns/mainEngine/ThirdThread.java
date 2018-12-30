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
import ns.worldLoad.WorldLoadMaster;
import ns.worldSave.SaveWorldMaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

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

		SaveWorldMaster.save(world,
				SaveData.openOutput("save" + WorldLoadMaster.count++ + "." + GU.WORLD_SAVE_FILE_FORMAT));
		try (OutputStream savesDirCount = SaveData.openOutput("saves.dir").asOutputStream()) {
			savesDirCount.write(Integer.toString(WorldLoadMaster.count).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		GU.currentThread().finishExecution();
	}

	private void execute(String ln) throws NumberFormatException {
		if (ln.equals("show-time")) {
			System.out.print("Simulation seconds since begun: " + GU.time.t + "; days passed in simulation "
					+ GU.time.day() + "\n>");
		} else if (ln.equals("h") || ln.equals("help")) {
			System.out.print("Commands available:\n\tshow-time --- shows the time since the begin of the "
					+ "simulation\n\th[elp] --- shows this list\n>");
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