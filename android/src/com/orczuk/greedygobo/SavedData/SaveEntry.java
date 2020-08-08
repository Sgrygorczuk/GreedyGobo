package com.orczuk.greedygobo.SavedData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SaveEntry {

    //Gets all the data to the entry
    public SaveEntry(String currentHighScore){ this.currentHighScore = currentHighScore; }

    @PrimaryKey
    public int uid;

    //Break down of the table
    @ColumnInfo(name = "currentHighScore")
    public String currentHighScore;

    public void delete() {
    }
}