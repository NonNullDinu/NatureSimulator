package obj;

import ns.utils.GU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import resources.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Materials extends ArrayList<Material> {
	private static final long serialVersionUID = -4711987157168197781L;

	public Materials(In resource) {
		this(GU.open(resource));
	}


	public Materials(BufferedReader reader) {
		String line;
		Material current = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("newmtl")) {
					if (current != null) {
						add(current);
					}
					current = new Material((byte) size());
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
			add(current);
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

	public List<Byte> asByteMaterials() {
		List<Byte> bytes = new ArrayList<>();
		for (Material m : this) {
			bytes.addAll(m.getBytes());
		}
		return bytes;
	}
}
