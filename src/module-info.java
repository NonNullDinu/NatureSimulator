/**
 * @author Dinu
 *
 */
module ns.gameModule {
	exports ns.mainEngine;
	exports ns.camera;
	exports ns.components;
	exports ns.configuration;
	exports ns.customFileFormat;
	exports ns.derrivedOpenGLObjects;
	exports ns.display;
	exports ns.entities;
	exports ns.exceptions;
	exports ns.flares;
	exports ns.fontMeshCreator;
	exports ns.fontRendering;
	exports ns.interfaces;
	exports ns.mainMenu;
	exports ns.openALObjects;
	exports ns.openglObjects;
	exports ns.openglWorkers;
	exports ns.options;
	exports ns.parallelComputing;
	exports ns.renderers;
	exports ns.rivers;
	exports ns.shaders;
	exports ns.terrain;
	exports ns.ui;
	exports ns.utils;
	exports ns.water;
	exports ns.world;
	exports ns.worldSave;
	exports ns.worldSave.NSSV1000;
    exports ns.worldSave.NSSV1100;
    exports ns.worldSave.NSSV1200;

    requires java.desktop;
	requires java.xml;
	requires lwjgl;
	requires lwjgl.util;
}