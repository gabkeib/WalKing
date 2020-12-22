package com.gryco.walking;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "results_table")
public class Results {

    public Results(String stepscount, String pointscount, String specificdate, String distance) {
        this.stepscount = stepscount;
        this.pointscount = pointscount;
        this.specificdate = specificdate;
        this.distance = distance;
    }

    /*@PrimaryKey(autoGenerate = true)
    private int id;*/

    @ColumnInfo(name = "steps_count")

    private String stepscount;

    @ColumnInfo(name = "points_count")
    private String pointscount;


    @ColumnInfo(name = "distance")
    private String distance;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String specificdate;


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStepscount() {
        return stepscount;
    }

    public void setStepscount(String stepscount) {
        this.stepscount = stepscount;
    }

    public String getPointscount() {
        return pointscount;
    }

    public void setPointscount(String pointscount) {
        this.pointscount = pointscount;
    }

    public String getSpecificdate() {
        return specificdate;
    }

    public void setSpecificdate(String specificdate) {
        this.specificdate = specificdate;
    }
}

