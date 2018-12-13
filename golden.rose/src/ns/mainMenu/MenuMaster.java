package ns.mainMenu;

import data.GameData;
import ns.ui.loading.UILoader;

public class MenuMaster {
	public static MainMenu menu;

	public static MainMenu createMainMenu() {
		return (MainMenu) UILoader
				.loadBinaryUIR(GameData.getResourceAt("uiResources/xml/MainMenu.uir").asInputStream());
	}
}