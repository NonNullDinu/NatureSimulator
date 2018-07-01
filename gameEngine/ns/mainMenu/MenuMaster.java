package ns.mainMenu;

import ns.ui.loading.UILoader;
import res.Resource;

public class MenuMaster {
	public static MainMenu menu;

	public static MainMenu createMainMenu() {
		return (MainMenu) UILoader
				.load(new Resource().withLocation("gameData/uiResources/xml/MainMenu.xml").create().asInputStream());
	}
}