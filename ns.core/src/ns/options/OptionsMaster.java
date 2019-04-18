/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ns.options;

import data.GameData;
import ns.ui.loading.UILoader;
import ns.utils.GU;

public class OptionsMaster {
	public static Options createOptions() {
//		List<Option> options = new ArrayList<>();
//		while (GU.Z003 == null)
//			Thread.yield();
//		options.add(new OnOffOption(new Vector2f(0, 0), new Vector2f(0.1f, 0.1f),
//				new GUIText("Some test option On", 1f, GU.Z003, new Vector2f(0, 0.02f), 0.2f, true)));
//		GUIButton back = new GUIButton(new Vector2f(-0.95f, -0.95f), new Vector2f(0.05f, 0.05f),
//				new TexFile("textures/back_arrow.tex").load());
//		return new Options(options, back);
		while (GU.Z003 == null || GU.caladea == null)
			Thread.yield();
		return (Options) UILoader
				.loadBinaryUIR(GameData.getResourceAt("uiResources/xml/Options.uir").asInputStream());
	}
}