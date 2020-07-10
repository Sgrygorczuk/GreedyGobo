package com.orczuk.greedygobo;

import com.badlogic.gdx.Game;

public class GreedyGobo extends Game {

	/*
	Input: Void
	Output: Void
	Purpose: Starts the app
	*/
	@Override
	public void create () { setScreen(new MainScreen()); }

}
