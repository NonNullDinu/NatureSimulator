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

package ns.ui.shop;

import ns.components.BlueprintCreator;
import ns.entities.Entity;
import ns.ui.ComplexGUI;
import ns.utils.GU;
import ns.utils.MousePicker;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class Shop {
    private static final float SLIDE_OFFSET = 0.75f;
    private final List<ShopItem> items;
    private final ComplexGUI complex;
    private ShopItem currentlySelected;
    private SS state;
    private int d;
    private boolean increasing;

    public Shop(List<ShopItem> items, ComplexGUI complex) {
        this.items = items;
        state = SS.OPEN;
        this.complex = complex;
    }

    public Entity update() {
        if (currentlySelected != null)
            if (d > 0) {
                if (increasing) {
                    d += 2;
                    increasing = d < 20;
                } else
                    d -= 2;
                GU.setMouseCursor(GU.createCursor(0, 0,
                        GU.getMouseTexture(currentlySelected.getEntityBlueprint().getFolder(), d), d));
            }
        if (GU.Key.KEY_S.pressedThisFrame()) {
            if (state != SS.OPEN) {
                if (state == SS.BUYING)
                    complex.getCenter().x += SLIDE_OFFSET;
                state = SS.OPEN;
            } else {
                state = SS.CLOSED;
            }
            currentlySelected = null;
            GU.setMouseCursor(GU.defaultCursor());
        }
        if (state == SS.CLOSED)
            return null;
        if (state == SS.OPEN) {
            for (ShopItem item : items) {
                if (item.clicked() && !GU.prevFrameClicked) {
                    currentlySelected = item;
                    state = SS.BUYING;
                    complex.getCenter().x -= SLIDE_OFFSET;
                    GU.setMouseCursor(GU.createCursor(0, 0,
                            GU.getMouseTexture(item.getEntityBlueprint().getFolder(), 0)));
                    return null;
                }
            }
            if (GU.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT) && GU.mouseDelta.length() == 0f && GU.lastFramesLengths == 0f) {
                state = SS.CLOSED;
                GU.setMouseCursor(GU.defaultCursor());
            }
        }
        if (state == SS.BUYING) {
            if (GU.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !GU.prevFrameClicked && GU.mouseDelta.length() == 0f
                    && GU.lastFramesLengths == 0f) {
                GU.setMouseCursor(GU.createCursor(0, 0,
                        GU.getMouseTexture(currentlySelected.getEntityBlueprint().getFolder(), 2), 2));
                d = 2;
                increasing = true;
                return new Entity(
                        BlueprintCreator.createBlueprintFor(currentlySelected.getEntityBlueprint().getFolder()),
                        MousePicker.getCurrentTerrainPoint());
            } else if (GU.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT) && GU.mouseDelta.length() == 0f && GU.lastFramesLengths == 0f) {
                complex.getCenter().x += SLIDE_OFFSET;
                state = SS.CLOSED;
                GU.setMouseCursor(GU.defaultCursor());
            }
        }
        return null;
    }

    public List<ShopItem> getItems() {
        return items;
    }

    public boolean open() {
        return state != SS.CLOSED;
    }

    public ComplexGUI getComplex() {
        return complex;
    }

    public void refreshCursor() {
        if (state == SS.BUYING)
            GU.setMouseCursor(GU.createCursor(0, 63,
                    GU.getMouseTexture(currentlySelected.getEntityBlueprint().getFolder(), 0)));
    }
}