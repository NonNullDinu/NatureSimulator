package ns.components;

import java.io.Serializable;

public interface IComponent extends Serializable {
	IComponent copy();
}