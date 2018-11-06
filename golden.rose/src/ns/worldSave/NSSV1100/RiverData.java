package ns.worldSave.NSSV1100;

import ns.rivers.River;
import ns.worldSave.Data;
import org.lwjgl.util.vector.Vector3f;

public class RiverData extends Data {
	private static final long serialVersionUID = -2850921767663909329L;

	private Vector3f source;

	public RiverData withSource(Vector3f source) {
		this.source = source;
		return this;
	}
	
	@Override
	public River asInstance() {
		return new River(source);
	}
}