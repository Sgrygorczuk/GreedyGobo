package com.orczuk.greedygobo.SavedData;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//Creates a database that will save data to the local machine
//We will only have one entry at a time that holds all of the info

public class SaveDatabase {
    @Database(entities = {SaveEntry.class}, version = 1)
    public abstract static class AppDatabase extends RoomDatabase {
        public abstract SaveDao saveDao();
    }
}
