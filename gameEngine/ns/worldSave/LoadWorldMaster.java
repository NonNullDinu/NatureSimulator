package ns.worldSave;

import ns.world.World;
import ns.worldSave.NSSV1000.NSSV1000File;
import ns.worldSave.NSSV1100.NSSV1100File;
import res.Resource;

public class LoadWorldMaster {
	public static World loadWorld(Resource res) {
		int ver = res.version();
		World world = null;
		if (ver == 1)
			world = new NSSV1000File(res).load();
		else if (ver == 2)
			world = new NSSV1100File(res).load();
		return world;
	}
}