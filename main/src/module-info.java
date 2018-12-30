/**
 * @author Dinu
 *
 */
module ns.gameModule {
	exports ns.mainEngine;

    requires java.desktop;
	requires lwjgl;
	requires lwjgl.util;
	requires golden.rose;
	requires jdk.unsupported; // Required by lwjgl
}