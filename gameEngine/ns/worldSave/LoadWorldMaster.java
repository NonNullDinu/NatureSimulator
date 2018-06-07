package ns.worldSave;

import ns.world.World;
import ns.worldSave.NSV1000.NSV1000File;
import res.Resource;

public class LoadWorldMaster {
	public static  World loadWorld(Resource res) {
		int ver = res.version();
		World world = null;
		if(ver == 1) {
			world = new NSV1000File(res).load();
		}
		return world;
	}
}