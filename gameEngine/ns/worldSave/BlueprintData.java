package ns.worldSave;

import ns.components.Blueprint;
import ns.components.BlueprintCreator;

public class BlueprintData extends Data {
	private static final long serialVersionUID = 8067084958250932904L;

	private String objectName;

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@Override
	public Blueprint asInstance() {
		return BlueprintCreator.createBlueprintFor(objectName);
	}
}