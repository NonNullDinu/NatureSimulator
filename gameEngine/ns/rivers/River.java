package ns.rivers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector3f;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.terrain.Terrain;
import ns.world.World;
import ns.worldSave.SerializableWorldObject;
import ns.worldSave.NSSV1100.RiverData;

public class River implements SerializableWorldObject {

	private List<WaterParticle> waterParticles = new ArrayList<>();
	private Vector3f source;
	private int cnt;
	private VAO model;

	public River(Vector3f source) {
		this.source = source;
		this.model = VAOLoader.storeDataInVAO(
				new VBOData(new float[] {}).withAttributeNumber(0).withDimensions(3).withUsage(GL15.GL_STREAM_DRAW));
	}

	public void update(World world) {
		cnt++;
		if (cnt == 30) {
			waterParticles.add(new WaterParticle(new Vector3f(source)));
			cnt = 0;
		}
		Terrain terrain = world.getTerrain();
		for (int i = waterParticles.size() - 1; i >= 0; i--) {
			WaterParticle particle = waterParticles.get(i);
			if (i == waterParticles.size() - 2)
				particle.updateVel(terrain);
			else {
				boolean remove = particle.update(terrain, source.y);
				if (remove) {
					waterParticles.remove(i);
					i++;
				}
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
		List<Float> list = new ArrayList<>();
		if (current.getVelocity().length() != 0) {
			Vector3f.cross(vel, UP, pos);
			pos.normalise();
			list.add(currentPos.x + pos.x * size);
			list.add(currentPos.y + riverHeight);
			list.add(currentPos.z + pos.z * size);

			list.add(currentPos.x - pos.x * size);
			list.add(currentPos.y + riverHeight);
			list.add(currentPos.z - pos.z * size);
		}
		for (int i = 1; i < waterParticles.size(); i++) {
			current = waterParticles.get(i);
			if (current.getVelocity().length() != 0) {
				currentPos = current.getPosition();
				pos = new Vector3f();
				vel = new Vector3f(current.getVelocity().x, current.deltaY(), current.getVelocity().y);
				Vector3f.cross(vel, UP, pos);
				size = current.getSize();
				pos.normalise();
				list.add(currentPos.x + pos.x * size);
				list.add(currentPos.y + riverHeight);
				list.add(currentPos.z + pos.z * size);

				list.add(currentPos.x - pos.x * size);
				list.add(currentPos.y + riverHeight);
				list.add(currentPos.z - pos.z * size);
			}
		}
		float[] vao_positions = listToArray(list);
		VAOLoader.recreateAndReplace(model, 0, vao_positions, GL15.GL_STREAM_DRAW);
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
		return 2 * waterParticles.size();
	}

	@Override
	public RiverData asData() {
		return new RiverData().withSource(source);
	}
}