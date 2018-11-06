package data;

import ns.utils.GU;
import resources.IReadWriteFolder;
import resources.In;
import resources.Out;

public class SaveData implements IReadWriteFolder {
	private static SaveData folder;
	
	public static void init() {
		folder = new SaveData();
	}

	private SaveData() {
	}

	@Override
	public In _getResourceAt(String location) {
		return In.create("saveData/" + location);
	}

	public static In getResourceAt(String location) {
		return folder._getResourceAt(location);
	}

	@Override
	public In _getResourceAt(String location, boolean version) {
		return In.create("saveData/" + location, version);
	}

	public static In getResourceAt(String location, boolean version) {
		return folder._getResourceAt(location, version);
	}

	@Override
	public Out _openOutput(String location) {
		return Out.create(GU.path + "/saveData/" + location);
	}

	public static Out openOutput(String location) {
		return folder._openOutput(location);
	}
}