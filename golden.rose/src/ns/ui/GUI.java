package ns.ui;

import ns.renderers.GUIRenderer;

public interface GUI {
	int ALPHA = 1;
	int POS_X = 2;
	int POS_Y = 3;
	int ROT = 4;

	void render(GUIRenderer renderer, float[] args, int... argType);
}