package com.orczuk.greedygobo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class GreedyGobo extends Game {

	private final AssetManager assetManager = new AssetManager();	//Holds the UI images and sound files

	private String currentHighScore = "0.00";						//Holds the current high score in local memory
	private boolean phoneFlag;										//Tells us if the game is played on phone or PC
	private boolean holyFlag = false;								//Tells us if the player at one point got -20 score
	private boolean holyInUseFlag = false;							//Tells us that this is the current skin selected
	private boolean blueFlag = false;								//Tells us if the player at one point got 75 score
	private boolean blueInUseFlag = false;							//Tells us that this is the current skin selected
	private boolean toxicFlag = false;								//Tells us if the player at one point got 150 score
	private boolean toxicInUseFlag = false;							//Tells us that this is the current skin selected

	/*
	Input: Boolean tells us if the game is started from Android or PC
	Output: Void
	Purpose: Tells the game what controls/information it should provide
	*/
	public GreedyGobo(boolean phone){this.phoneFlag = phone;}

	/*
	Input: Void
	Output: Asset Manager
	Purpose: Returns asset manager with all its data
	*/
	AssetManager getAssetManager() { return assetManager; }

	/*
	Input: String
	Output: Void
	Purpose: Updates the high score in game and database
	*/
	public void setCurrentHighScore(String highScore){currentHighScore = highScore;}

	/*
	Input: Void
	Output: String
	Purpose: Gives back the current high score
	*/
	public String getCurrentHighScore(){return currentHighScore;}


	/*
	Input: String
	Output: Void
	Purpose: Updates the holy Flag
	*/
	public void setHoly(Boolean holyFlag){this.holyFlag = holyFlag;}
	public boolean getHoly(){return holyFlag;}

	public boolean getHolyInUse(){return holyInUseFlag;}
	public void setHolyInUse(Boolean holyInUseFlag){this.holyInUseFlag = holyInUseFlag;}

	public void setBlue(Boolean blueFlag){this.blueFlag = blueFlag;}
	public boolean getBlue(){return blueFlag;}

	public boolean getBlueInUse(){return blueInUseFlag;}
	public void setBlueInUse(Boolean blueInUseFlag){this.blueInUseFlag = blueInUseFlag;}

	public void setToxic(Boolean toxicFlag){this.toxicFlag = toxicFlag;}
	public boolean getToxic(){return toxicFlag;}

	public boolean getToxicInUse(){return toxicInUseFlag;}
	public void setToxicInUse(Boolean toxicInUseFlag){this.toxicInUseFlag = toxicInUseFlag;}


	/*
	Input: Void
	Output: Boolean
	Purpose: Tells us what device we are on
	*/
	public boolean getPhone(){return phoneFlag;}

	/*
	Input: Void
	Output: Void
	Purpose: Starts the app
	*/
	@Override
	public void create () { setScreen(new LoadingScreen(this)); }

}
