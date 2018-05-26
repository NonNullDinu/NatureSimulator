package ns.world;

import java.util.List;

import ns.entities.Entity;
import ns.terrain.Terrain;

public class World {
	private List<Entity> entities;
	private Terrain terrain;

	public World(List<Entity> entities, Terrain terrain) {
		this.entities = entities;
		this.terrain = terrain;
	}
	
	public void update() {
		for(Entity e : entities)
			e.update(this);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void add(Entity e) {
		entities.add(e);
		if(e.getBiomeSpreadComponent() != null)
			terrain.updateColors(e);
	}
}