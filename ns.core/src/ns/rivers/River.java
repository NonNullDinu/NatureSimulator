/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
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

package ns.rivers;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.terrain.Terrain;
import ns.world.World;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class River implements Serializable {
	static final int COUNT = 10;
	private static final long serialVersionUID = 3885806111259199169L;
	private final List<WaterParticle> waterParticles = new ArrayList<>();
	private final Vector3f source;
	private int cnt;
	private transient VAO model;
	private RiverEnd riverEnd;
	private transient boolean addEnd;
	private transient boolean dec;
	private int idx;

	private int sub;

	public River(Vector3f source) {
		this.source = source;
		this.model = VAOLoader.storeDataInVAO(
				new VBOData(new float[]{}).withAttributeNumber(0).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW),
				new VBOData(new float[]{}).withAttributeNumber(1).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW),
				new VBOData(new float[]{}).withAttributeNumber(2).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.model = VAOLoader.storeDataInVAO(
				new VBOData(new float[]{}).withAttributeNumber(0).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW),
				new VBOData(new float[]{}).withAttributeNumber(1).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW),
				new VBOData(new float[]{}).withAttributeNumber(2).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW));
		addEnd = riverEnd != null;
	}

	public void update(World world) {
		if (dec)
			cnt--;
		else
			cnt++;
		if (cnt == 0 || cnt == COUNT) {
			waterParticles.add(new WaterParticle(new Vector3f(source), idx++,
					waterParticles.size() == 0 ? null : waterParticles.get(waterParticles.size() - 1)));
			dec = cnt == COUNT;
		}
		Terrain terrain = world.getTerrain();
		for (int i = waterParticles.size() - 1; i >= 0; i--) {
			WaterParticle particle = waterParticles.get(i);
			if (i == 0) {
				if (particle.getPrev() != null) {
					particle.setPosition(new Vector3f(particle.getPrev().getPrevPosition()), this.source.y);
				} else {
					for (River r : world.getRivers()) {
						if (r.equals(this))
							continue;
						for (WaterParticle p : r.waterParticles) {
							if (Vector3f.sub(p.getPosition(), particle.getPosition(), null).lengthSquared() < particle.getSize()) {
								particle.setPrevious(p);
								p.addFollows(particle);
								i = -1;
								continue;
							}
						}
					}
					boolean remove = particle.update(terrain, source.y);
					if (remove) {
						particle.removeFromFollowers();
						if (!particle.reachedBaseLake()) {
							if (this.riverEnd == null) {
								this.riverEnd = new RiverEnd(particle.getPosition());
								terrain.addRiverEnd(this.riverEnd);
							}
						}
						waterParticles.remove(i);
//						i++;
						if (waterParticles.size() == 0)
							return;
					}
				}
//			} else if (i == waterParticles.size() - 1) {
//				particle.updateVel(terrain);
//				System.out.println(particle.getPosition().toString());
			} else {
				particle.setPosition(new Vector3f(particle.getPrev().getPrevPosition()), source.y);
			}
		}
		if (waterParticles.size() < 2)
			return;
//		System.out.println(waterParticles.size());
		WaterParticle current = waterParticles.get(0);
		Vector3f currentPos = current.getPosition();
		Vector3f pos = new Vector3f();
		Vector3f vel = new Vector3f(current.getVelocity().x, current.deltaY(), current.getVelocity().y);
		final Vector3f UP = new Vector3f(0, 1, 0);
		final float riverHeight = 0.8f;
		float size = current.getSize();
		List<Float> list1 = new ArrayList<>();
		List<Float> list2 = new ArrayList<>();
		List<Float> list3 = new ArrayList<>();
		sub = 0;
		if (current.getVelocity().lengthSquared() != 0) {
			Vector3f.cross(vel, UP, pos);
			pos.normalise();
			list1.add(currentPos.x + pos.x * size);
			list1.add(currentPos.y);
			list1.add(currentPos.z + pos.z * size);

			list1.add(currentPos.x - pos.x * size);
			list1.add(currentPos.y);
			list1.add(currentPos.z - pos.z * size);

			list2.add(currentPos.x + pos.x * size);
			list2.add(currentPos.y);
			list2.add(currentPos.z + pos.z * size);

			list2.add(currentPos.x + pos.x * size * 2);
			list2.add(currentPos.y);
			list2.add(currentPos.z + pos.z * size * 2);

			list3.add(currentPos.x - pos.x * size);
			list3.add(currentPos.y);
			list3.add(currentPos.z - pos.z * size);

			list3.add(currentPos.x - pos.x * size * 2);
			list3.add(currentPos.y);
			list3.add(currentPos.z - pos.z * size * 2);
		} else {
			sub = 1;
		}
		for (int i = 1; i < waterParticles.size(); i++) {
			current = waterParticles.get(i);
			currentPos = current.getPosition();
			if (current.getVelocity().lengthSquared() != 0 && current.getPosition().lengthSquared() != 0f) {
				pos = new Vector3f();
				vel = new Vector3f(current.getVelocity().x, current.deltaY(), current.getVelocity().y);
				Vector3f.cross(vel, UP, pos);
				size = current.getSize();
				pos.normalise();
				list1.add(currentPos.x + pos.x * size);
				list1.add(currentPos.y + riverHeight * size / 3f);
				list1.add(currentPos.z + pos.z * size);

				list1.add(currentPos.x - pos.x * size);
				list1.add(currentPos.y + riverHeight * size / 3f);
				list1.add(currentPos.z - pos.z * size);

				list2.add(currentPos.x + pos.x * size);
				list2.add(currentPos.y + riverHeight * size / 3f);
				list2.add(currentPos.z + pos.z * size);

				list2.add(currentPos.x + pos.x * size * 2);
				list2.add(currentPos.y);
				list2.add(currentPos.z + pos.z * size * 2);

				list3.add(currentPos.x - pos.x * size);
				list3.add(currentPos.y + riverHeight * size / 3f);
				list3.add(currentPos.z - pos.z * size);

				list3.add(currentPos.x - pos.x * size * 2);
				list3.add(currentPos.y);
				list3.add(currentPos.z - pos.z * size * 2);
			} else {
				sub++;
			}
		}
		VAOLoader.recreateAndReplace(model, 0, listToArray(list1), GL15.GL_STREAM_DRAW);
		VAOLoader.recreateAndReplace(model, 1, listToArray(list2), GL15.GL_STREAM_DRAW);
		VAOLoader.recreateAndReplace(model, 2, listToArray(list3), GL15.GL_STREAM_DRAW);

		if (addEnd) {
			world.getTerrain().addRiverEnd(riverEnd);
			addEnd = false;
		}

		if (this.riverEnd != null) {
			this.riverEnd.update(world);
		}
	}

	private float[] listToArray(List<Float> list) {
		float[] arr = new float[list.size()];
		for (int i = 0; i < arr.length; i++)
			arr[i] = list.get(i);
		return arr;
	}

	public VAO getModel() {
		return model;
	}

	public int vao_length() {
		return 2 * (waterParticles.size() - sub);
	}
}