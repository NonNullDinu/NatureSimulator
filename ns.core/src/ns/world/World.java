package ns.world;

import ns.entities.Entity;
import ns.rivers.RiverList;
import ns.terrain.Terrain;

import java.util.List;

public class World {
	private List<Entity> entities;
	private Terrain terrain;
	private RiverList rivers;

	public World(List<Entity> entities, Terrain terrain) {
		this.entities = entities;
		this.terrain = terrain;
		terrain.setWorld(this);
	}
	
	public void update() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update(this);
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
	
	public void setRivers(RiverList rivers) {
		this.rivers = rivers;
	}

	public RiverList getRivers() {
		return rivers;
	}

	public void remove(Entity entity) {
		entities.remove(entity);
	}
}