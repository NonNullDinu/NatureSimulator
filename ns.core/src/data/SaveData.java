package data;

import resources.IFolder;
import resources.Resource;

public class SaveData implements IFolder {
	private static SaveData folder;
	
	public static void init() {
		folder = new SaveData();
	}

	private SaveData() {
	}

	@Override
	public Resource _getResourceAt(String location) {
		return Resource.create("saveData/" + location);
	}

	public static Resource getResourceAt(String location) {
		return folder._getResourceAt(location);
	}

	@Override
	public Resource _getResourceAt(String location, boolean version) {
		return Resource.create("saveData/" + location, version);
	}

	public static Resource getResourceAt(String location, boolean version) {
		return folder._getResourceAt(location, version);
	}
}