package ns.rivers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import ns.terrain.Terrain;
import ns.world.World;
import ns.worldSave.SerializableWorldObject;
import ns.worldSave.NSSV1100.RiverData;

public class River implements SerializableWorldObject {
	private List<WaterParticle> waterParticles = new ArrayList<>();
	private Vector3f source;
	private int cnt;

	public River(Vector3f source) {
		this.source = source;
	}

	public void update(World world) {
		Terrain terrain = world.getTerrain();
		for (int i = waterParticles.size() - 1; i > 0; i--) {
			WaterParticle particle = waterParticles.get(i);
			boolean remove = particle.update(terrain);
			if (remove) {
				waterParticles.remove(i);
				i--;
			}
		}
		cnt++;
		if (cnt == 30) {
			waterParticles.add(new WaterParticle(new Vector3f(source)));
			cnt = 0;
		}
	}

	public List<WaterParticle> getParticles() {
		return waterParticles;
	}

	@Override
	public RiverData asData() {
		return new RiverData().withSource(source);
	}
}