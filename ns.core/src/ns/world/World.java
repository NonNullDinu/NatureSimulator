package ns.world;

import ns.entities.Entity;
import ns.entities.EntitySelectingCriteria;
import ns.rivers.RiverList;
import ns.terrain.Terrain;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class World {
	private final List<Entity> entities;
	private final Terrain terrain;
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
		if (e.getBiomeSpreadComponent() != null)
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

	public Entity closestEntity(Vector3f pos, EntitySelectingCriteria criteria) {
		Entity e = null;
		float md = 1e8f;
		for (Entity entity : entities) {
			float l;
			if (criteria.accepts(entity) && (l = Vector3f.sub(entity.getPosition(), pos, null).lengthSquared()) < md) {
				e = entity;
				md = l;
			}
		}
		return e;
	}
}