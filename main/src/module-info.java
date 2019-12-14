/**
 * @author Dinu
 *
 */
module ns.gameModule {
	exports ns.mainEngine;

    requires org.lwjgl;
    requires org.lwjgl.glfw;
    requires org.lwjgl.assimp;
    requires org.lwjgl.openal;
    requires org.lwjgl.opengl;
    requires org.lwjgl.natives;
    requires org.lwjgl.glfw.natives;
    requires org.lwjgl.assimp.natives;
    requires org.lwjgl.openal.natives;
    requires org.lwjgl.opengl.natives;

    requires java.desktop;
	requires lwjgl.util;
	requires ns.core;
	requires jdk.unsupported; // Required by lwjgl
}