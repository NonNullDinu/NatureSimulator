/**
 * The core module
 *
 * @author Dinu
 */
module golden.rose {
	requires java.xml;
	requires java.se;
	requires transitive lwjgl;
	requires transitive lwjgl.util;

	exports data;
	exports obj;
	exports resources;
	exports ns.parallelComputing;
	exports ns.worldSave;
	exports ns.worldSave.NSSV1000;
	exports ns.worldSave.NSSV1100;
	exports ns.worldSave.NSSV1200;
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
	exports ns.renderers;
	exports ns.rivers;
	exports ns.shaders;
	exports ns.terrain;
	exports ns.ui;
	exports ns.ui.loading;
	exports ns.ui.shop;
	exports ns.utils;
	exports ns.water;
	exports ns.world;
	exports ns.utils.natives;
	exports ns.time;
	exports ns.ui.loadingScreen;
	exports ns.worldLoad;
	exports ns.shaders.uniformStructs;
}