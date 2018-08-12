package ns.worldSave;

import java.io.Serializable;

public abstract class Data implements Serializable {
	private static final long serialVersionUID = -6430168927862876997L;
	
	public abstract Object asInstance();
}