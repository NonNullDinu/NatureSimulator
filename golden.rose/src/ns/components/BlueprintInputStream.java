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

package ns.components;

import ns.genetics.DNA;
import ns.openglWorkers.ModelsLibrary;
import ns.world.Biome;
import org.lwjgl.util.vector.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;

public class BlueprintInputStream {
	private final ObjectInputStream in;

	public BlueprintInputStream(ObjectInputStream in) {
		this.in = in;
	}

	public Blueprint read() throws IOException, ClassNotFoundException {
		byte[] data = new byte[8];
		in.read(data);

		int dataIn = data[0];
		String enFolder = Integer.toString(999 + dataIn);
		String modelName = "";
		boolean useMovementHeightStop = false;
		float heightStop = 0f;
		switch (dataIn) {
			case 1:
			case 2:
			case 3:
			case 5:
			case 6:
			case 7:
			case 11:
				modelName = "tree";
				break;
			case 4:
				modelName = "mushroom";
				break;
			case 8:
				modelName = "grass";
				useMovementHeightStop = true;
				heightStop = 0.4f;
				break;
			case 9:
				modelName = "stone";
				break;
			case 10:
				modelName = "seaWeed";
				break;
			case 12:
				modelName = "snowman";
				break;
			case 13:
				modelName = "sheep";
				break;
			case 14:
				modelName = "meat";
				break;
		}
		Blueprint blueprint = new Blueprint(enFolder);
		ModelComponent mc = new ModelComponent(
				ModelsLibrary.getModel("models/" + enFolder + "/" + modelName + ".mdl"));
		if (useMovementHeightStop)
			mc.useHeightStopMovement(heightStop);

		blueprint.withModel(mc);
		if (data[1] >> 4 != 0) {
			blueprint.withMovement(new MovementComponent(data[1] >> 4));
		}
		if ((data[1] & 15) != 0) {
			blueprint.withBiomeSpread(
					new BiomeSpreadComponent().withBiome(Biome.get(data[1] & 15)).withMinMaxRange(data[2], data[3]));
		}
		if (data[4] != 0) {
			blueprint.withLife((data[6] & 0x7F) != 0x40 ?
					new PlantLifeComponent(data[4] << 8 + data[5]).withLimits(new Vector2f(data[6], data[7])) :
					new AnimalLifeComponent(data[4] << 8 + data[5]).withReprTime((data[6] & 0x3F) * 60f).withDNA((DNA) in.readObject()));
		}
		blueprint.withDefaultCustomColors();
		return blueprint;
	}

	public Blueprint read_new() throws IOException, ClassNotFoundException {
		Blueprint b = (Blueprint) in.readObject();
		int dataIn = Integer.parseInt(b.getFolder()) - 999;
		String modelName = null;

		switch (dataIn) {
			case 1:
			case 2:
			case 3:
			case 5:
			case 6:
			case 7:
			case 11:
				modelName = "tree";
				break;
			case 4:
				modelName = "mushroom";
				break;
			case 8:
				modelName = "grass";
				break;
			case 9:
				modelName = "stone";
				break;
			case 10:
				modelName = "seaWeed";
				break;
			case 12:
				modelName = "snowman";
				break;
			case 13:
				modelName = "sheep";
				break;
			case 14:
				modelName = "meat";
				break;
		}
		b.setModel(ModelsLibrary.getModel("models/" + b.getFolder() + "/" + modelName + ".mdl"));
		return b;
	}
}
