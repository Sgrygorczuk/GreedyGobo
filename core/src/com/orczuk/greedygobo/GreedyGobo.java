package com.orczuk.greedygobo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class GreedyGobo extends Game {

	private final AssetManager assetManager = new AssetManager();	//Holds the UI images and sound files

	private String currentHighScore = "50.32";

	/*
	Input: Void
	Output: Asset Manager
	Purpose: Returns asset manager with all its data
	*/
	AssetManager getAssetManager() { return assetManager; }

	public void setCurrentHighScore(String highScore){currentHighScore = highScore;}
	public String getCurrentHighScore(){return currentHighScore;}

	/*
	Input: Void
	Output: Void
	Purpose: Starts the app
	*/
	@Override
	public void create () { setScreen(new LoadingScreen(this)); }

}
