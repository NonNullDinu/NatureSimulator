package ns.rivers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector3f;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.terrain.Terrain;
import ns.world.World;

public class River implements Serializable {
	private static final long serialVersionUID = 3885806111259199169L;

	protected static final int COUNT = 15;
	private List<WaterParticle> waterParticles = new ArrayList<>();
	private Vector3f source;
	private int cnt;
	private transient VAO model;
	private RiverEnd riverEnd;
	private transient boolean addEnd;
	private transient boolean dec;

	private int sub;

	public River(Vector3f source) {
		this.source = source;
		this.model = VAOLoader.storeDataInVAO(
				new VBOData(new float[] {}).withAttributeNumber(0).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW),
				new VBOData(new float[] {}).withAttributeNumber(1).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW),
				new VBOData(new float[] {}).withAttributeNumber(2).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.model = VAOLoader.storeDataInVAO(
				new VBOData(new float[] {}).withAttributeNumber(0).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW),
				new VBOData(new float[] {}).withAttributeNumber(1).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW),
				new VBOData(new float[] {}).withAttributeNumber(2).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW));
		addEnd = riverEnd != null;
	}

	public void update(World world) {
		if (dec)
			cnt--;
		else
			cnt++;
		if (cnt == 0 || cnt == COUNT) {
			waterParticles.add(new WaterParticle(new Vector3f(source)));
			dec = cnt == COUNT;
		}
		Terrain terrain = world.getTerrain();
		for (int i = waterParticles.size() - 1; i >= 0; i--) {
			WaterParticle particle = waterParticles.get(i);
			if (i == 0) {
				boolean remove = particle.update(terrain, source.y);
				if (remove) {
					if (!particle.reachedBaseLake()) {
						if (this.riverEnd == null) {
							this.riverEnd = new RiverEnd(particle.getPosition());
							terrain.addRiverEnd(this.riverEnd);
						}
					}
					waterParticles.remove(i);
					i++;
					if (waterParticles.size() == 0)
						return;
				}
			} else if (i == waterParticles.size() - 1) {
				particle.updateVel(terrain);
			} else {
				WaterParticle prev = waterParticles.get(i - 1);
				particle.setPosition(new Vector3f(prev.getPrevPosition()), source.y, prev.getVelocity(), prev.deltaY());
			}
		}
		if (waterParticles.size() < 2)
			return;
		WaterParticle current = waterParticles.get(0);
		Vector3f currentPos = current.getPosition();
		Vector3f pos = new Vector3f();
		Vector3f vel = new Vector3f(current.getVelocity().x, current.deltaY(), current.getVelocity().y);
		final Vector3f UP = new Vector3f(0, 1, 0);
		final float riverHeight = 0.5f;
		float size = current.getSize();
		List<Float> list1 = new ArrayList<>();
		List<Float> list2 = new ArrayList<>();
		List<Float> list3 = new ArrayList<>();
		if (current.getVelocity().length() != 0) {
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
		}
		sub = 0;
		final float wave = 0.2f * (float) cnt / (float) COUNT;
		for (int i = 1; i < waterParticles.size(); i++) {
			current = waterParticles.get(i);
			if (current.getVelocity().length() != 0 && currentPos.length() != 0) {
				currentPos = current.getPosition();
				pos = new Vector3f();
				vel = new Vector3f(current.getVelocity().x, current.deltaY(), current.getVelocity().y);
				Vector3f.cross(vel, UP, pos);
				size = current.getSize();
				pos.normalise();
				list1.add(currentPos.x + pos.x * size);
				list1.add(currentPos.y + riverHeight + (i % 2 == 0 ? (wave) : (-wave)));
				list1.add(currentPos.z + pos.z * size);

				list1.add(currentPos.x - pos.x * size);
				list1.add(currentPos.y + riverHeight + (i % 2 == 0 ? (wave) : (-wave)));
				list1.add(currentPos.z - pos.z * size);

				list2.add(currentPos.x + pos.x * size);
				list2.add(currentPos.y + riverHeight + (i % 2 == 0 ? (wave) : (-wave)));
				list2.add(currentPos.z + pos.z * size);

				list2.add(currentPos.x + pos.x * size * 2);
				list2.add(currentPos.y);
				list2.add(currentPos.z + pos.z * size * 2);

				list3.add(currentPos.x - pos.x * size);
				list3.add(currentPos.y + riverHeight + (i % 2 == 0 ? (wave) : (-wave)));
				list3.add(currentPos.z - pos.z * size);

				list3.add(currentPos.x - pos.x * size * 2);
				list3.add(currentPos.y);
				list3.add(currentPos.z - pos.z * size * 2);
			}else {
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

	public List<WaterParticle> getParticles() {
		return waterParticles;
	}

	public VAO getModel() {
		return model;
	}

	public int vao_length() {
		return 2 * (waterParticles.size() - sub);
	}

//	@Override
//	public RiverData asData() {
//		return new RiverData().withSource(source);
//	}
}