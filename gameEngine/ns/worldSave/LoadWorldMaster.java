package ns.worldSave;

import ns.world.World;
import ns.worldSave.NSSV1000.NSSV1000File;
import res.Resource;

public class LoadWorldMaster {
	public static  World loadWorld(Resource res) {
		int ver = res.version();
		World world = null;
		if(ver == 1) {
			world = new NSSV1000File(res).load();
		}
		return world;
	}
}