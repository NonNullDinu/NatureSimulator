/**
 * The game module
 * 
 * @author Dinu B.
 */
module GameModule {
	// Dependencies
	requires java.se;
	requires transitive lwjgl;
	requires transitive lwjgl.util;
	
	// src
	exports ns.mainEngine;

	// audioEngine
	exports ns.openALObjects;
	
	// resources
	exports obj;
	exports res;
	
	//gameEngine
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
	
	// renderEngine
	exports ns.display;
	exports ns.openglObjects;
	exports ns.openglWorkers;
	exports ns.renderers;
	exports ns.shaders;
	exports ns.shaders.uniformStructs;

	// Shaders
	opens ns.shaders.uniformStructs;
	opens res.shaders.standard;
	opens res.shaders.terrain;
	opens res.shaders.water;
	opens res.shaders.depthFieldBlur;
	opens res.shaders.blur;
	opens res.shaders.guis;
	opens res.shaders.menuDNA;
	opens res.shaders.colorQuad;

	// Textures
	opens res.textures.buttonTextures;

	// Models
	opens res.models.others;
	
	//Game configuration
	opens ns.configuration;
}