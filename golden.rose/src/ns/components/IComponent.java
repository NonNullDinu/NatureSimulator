package ns.components;

import java.io.Serializable;

interface IComponent extends Serializable {
	IComponent copy();
}