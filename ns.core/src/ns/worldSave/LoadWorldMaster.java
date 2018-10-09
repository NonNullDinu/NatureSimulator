package ns.worldSave;

import ns.world.World;
import ns.worldSave.NSSV1000.NSSV1000File;
import ns.worldSave.NSSV1100.NSSV1100File;
import ns.worldSave.NSSV1200.NSSV1200File;
import ns.worldSave.NSSV1300.NSSV1300File;
import resources.In;

public class LoadWorldMaster {
	public static World loadWorld(In res) {
		int ver = res.version();
		World world = null;
		if (ver == 1)
			world = new NSSV1000File(res).load();
		else if (ver == 2)
			world = new NSSV1100File(res).load();
		else if (ver == 3)
			world = new NSSV1200File(res).load();
		else if (ver == 4)
			world = new NSSV1300File(res).load();
		return world;
	}
}