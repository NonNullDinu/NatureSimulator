/**
 * @author Dinu
 *
 */
module ns.gameModule {
	exports ns.mainEngine;

    requires java.desktop;
	requires lwjgl;
	requires lwjgl.util;
	requires ns.core;
	requires jdk.unsupported; // Required by lwjgl
}