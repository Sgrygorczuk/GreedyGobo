package com.orczuk.greedygobo;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.room.Room;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.orczuk.greedygobo.GreedyGobo;
import com.orczuk.greedygobo.SavedData.SaveDatabase;
import com.orczuk.greedygobo.SavedData.SaveEntry;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class AndroidLauncher extends AndroidApplication {


	//Timer and task used to save data
	Timer timer = new Timer();

	/*
	Input: Void
	Output: Void
	Purpose: Saves the data to the local storage
	*/
	TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			//Sets up a info to input into the database
			SaveEntry saveEntry = new SaveEntry(greedyGobo.getCurrentHighScore(), greedyGobo.getHoly(), greedyGobo.getBlue(), greedyGobo.getToxic());
			//Updates that input
			saveDB.saveDao().updateEntry(saveEntry);
		}
	};

	/*
	Input: Void
	Output: Void
	Purpose: When app turns on it loads saved data, if there is no saved data makes saved data then loads it
	*/
	TimerTask loadTask = new TimerTask() {
		@Override
		public void run() {
			//If we have never saved data make an initial save then load it
			if (saveDB.saveDao().getAll().isEmpty()) {
				SaveEntry saveEntry = new SaveEntry(greedyGobo.getCurrentHighScore(), greedyGobo.getHoly(), greedyGobo.getBlue(), greedyGobo.getToxic());
				saveDB.saveDao().insertAll(saveEntry);
			}
			//Loads the data
			greedyGobo.setCurrentHighScore(saveDB.saveDao().getAll().get(0).currentHighScore);
			greedyGobo.setHoly(saveDB.saveDao().getAll().get(0).holyFlag);
			greedyGobo.setBlue(saveDB.saveDao().getAll().get(0).blueFlag);
			greedyGobo.setToxic(saveDB.saveDao().getAll().get(0).toxicFlag);

		}
	};

	SaveDatabase.AppDatabase saveDB;			//The database that accesses local storage
	//The app
	GreedyGobo greedyGobo = new GreedyGobo(true);


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		saveDB = Room.databaseBuilder(getApplicationContext(), SaveDatabase.AppDatabase.class, "saveentry").build();
		timer.schedule(loadTask, 0);									//Loads in data from the saved file
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps the screen lit up
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;										//Allows the use of Accelerometer readings
		config.useImmersiveMode = true;										//Makes the navigation bar go away
		initialize(greedyGobo, config);										//Starts the game
		//Every 60 sec save the data to data base
		timer.scheduleAtFixedRate(updateTask, 3000, 6000);
	}
}
