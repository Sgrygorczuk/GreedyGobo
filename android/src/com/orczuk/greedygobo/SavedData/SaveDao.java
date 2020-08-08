package com.orczuk.greedygobo.SavedData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


//We will only store one entry in the database

@Dao
public interface SaveDao {
    //Returns the one entry
    @Query("SELECT * FROM saveentry")
    List<SaveEntry> getAll();

    //Adds in an entry
    @Insert
    void insertAll(SaveEntry... saveEntries);

    //Cleans up the entry
    @Query("DELETE FROM saveentry")
    void delete();
}
