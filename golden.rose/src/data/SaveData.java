package data;

import ns.utils.GU;
import resources.IReadWriteFolder;
import resources.In;
import resources.Out;

public class SaveData implements IReadWriteFolder {
	private static SaveData folder;

	private SaveData() {
	}

	public static void init() {
		folder = new SaveData();
	}

	public static In getResourceAt(String location) {
		return folder._getResourceAt(location);
	}

	public static In getResourceAt(String location, boolean version) {
		return folder._getResourceAt(location, version);
	}

	public static Out openOutput(String location) {
		return folder._openOutput(location);
	}

	@Override
	public In _getResourceAt(String location) {
		return In.create("saveData/" + location);
	}

	@Override
	public In _getResourceAt(String location, boolean version) {
		return In.create("saveData/" + location, version);
	}

	@Override
	public Out _openOutput(String location) {
		return Out.create(GU.path + "/saveData/" + location);
	}
}