/**
 * The game module
 * 
 * @author Dinu B.
 */
open module GameModule {
	// Dependencies
	requires java.desktop;
	requires lwjgl;
	requires lwjgl.util;

	// src
	exports ns.mainEngine;

	// audioEngine
	exports ns.openALObjects;

	// gameEngine
	exports ns.camera;
	exports ns.components;
	exports ns.configuration;
	exports ns.customFileFormat;
	exports ns.entities;
	exports ns.exceptions;
	exports ns.mainMenu;
	exports ns.parallelComputing;
	exports ns.terrain;
	exports ns.ui;
	exports ns.utils;
	exports ns.water;
	exports ns.world;
	exports ns.worldSave;
	exports res;
	exports obj;

	// renderEngine
	exports ns.display;
	exports ns.openglObjects;
	exports ns.openglWorkers;
	exports ns.renderers;
	exports ns.shaders;
	exports ns.shaders.uniformStructs;
}