package obj;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ns.utils.GU;
import res.Resource;

public class Materials extends ArrayList<Material> {
	private static final long serialVersionUID = -4711987157168197781L;

	public Materials(Resource resource) {
		BufferedReader reader = GU.open(resource);
		String line;
		Material current = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("newmtl")) {
					if (current != null) {
						super.add(current);
					}
					current = new Material();
					current.setName(line.split(" ")[1]);
				} else if (line.startsWith("Kd")) {
					String[] pcs = line.split(" ");
					current.setColor(
							new Vector3f(Float.parseFloat(pcs[1]), Float.parseFloat(pcs[2]), Float.parseFloat(pcs[3])));
				} else if (line.startsWith("Ka")) {
					String[] pcs = line.split(" ");
					current.setIndicators(
							new Vector3f(Float.parseFloat(pcs[1]), Float.parseFloat(pcs[2]), Float.parseFloat(pcs[3])));
				} else if (line.startsWith("d")) {
					String[] pcs = line.split(" ");
					current.setData(new Vector4f(Float.valueOf(pcs[1]), 0, 0, 0));
				}
			}
			super.add(current);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Material get(String name) {
		for (Material m : this) {
			if (m.getName().compareTo(name) == 0)
				return m;
		}
		return null;
	}
}
