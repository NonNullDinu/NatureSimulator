package ns.rivers;

import java.util.ArrayList;

import ns.world.World;

public class RiverList extends ArrayList<River> {
	private static final long serialVersionUID = -5312208733487768061L;

	public void update(World world) {
		for(River r : this)
			r.update(world);
	}
}