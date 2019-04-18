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
	private boolean updateColors;

	public World(List<Entity> entities, Terrain terrain) {
		this.entities = entities;
		this.terrain = terrain;
		terrain.setWorld(this);
	}

	public void update() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update(this);
		if (updateColors) {
			updateColors = false;
			terrain.initColors(entities);
		}
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

	public RiverList getRivers() {
		return rivers;
	}

	public void setRivers(RiverList rivers) {
		this.rivers = rivers;
	}

	public void remove(Entity entity) {
		entities.remove(entity);
		if (entity.getBiomeSpreadComponent() != null)
			updateColors = true;
	}

	public Entity closestEntity(Vector3f pos, EntitySelectingCriteria criteria) {
		Entity e = null;
		float md = 1e8f;
		float l;
		for (Entity entity : entities) {
			if (criteria.accepts(entity) && (l = Vector3f.sub(entity.getPosition(), pos, null).lengthSquared()) < md) {
				e = entity;
				md = l;
			}
		}
		return e;
	}
}