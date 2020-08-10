package com.orczuk.greedygobo.SavedData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SaveEntry {

    //Gets all the data to the entry
    public SaveEntry(String currentHighScore, Boolean holyFlag, Boolean blueFlag, Boolean toxicFlag){
        uid = 0;
        this.currentHighScore = currentHighScore;
        this.holyFlag = holyFlag;
        this.blueFlag = blueFlag;
        this.toxicFlag = toxicFlag;
    }

    @PrimaryKey
    public int uid;

    //Break down of the table
    @ColumnInfo(name = "currentHighScore")
    public String currentHighScore;
    @ColumnInfo(name = "holyFlag")
    public Boolean holyFlag;
    @ColumnInfo(name = "blueFlag")
    public Boolean blueFlag;
    @ColumnInfo(name = "toxicFlag")
    public Boolean toxicFlag;

}