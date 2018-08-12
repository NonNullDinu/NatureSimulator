package data;

import java.io.IOException;
import java.io.InputStream;

import resources.IFolder;
import resources.Resource;

public class GameData implements IFolder {
	private static GameData folder;

	public static void init() {
		InputStream ins = Resource.create("gameData/.fv").asInputStream();
		try {
			byte[] bytes = ins.readAllBytes();
			String s = new String(bytes, 0, bytes.length);
			folder = new GameData(Integer.parseInt(s.replace(".", "")));
		} catch (IOException e) {
		}
	}

	private GameData(int version) {
		if (version != 11) {
			System.err.println("Unable to load resources: game folder version unknown");
			System.exit(-1);
		}
	}

	@Override
	public Resource _getResourceAt(String location) {
		return Resource.create("gameData/" + location);
	}

	public static Resource getResourceAt(String location) {
		return folder._getResourceAt(location);
	}

	@Override
	public Resource _getResourceAt(String location, boolean version) {
		return Resource.create("gameData/" + location, version);
	}

	public static Resource getResourceAt(String location, boolean version) {
		return folder._getResourceAt(location, version);
	}
}