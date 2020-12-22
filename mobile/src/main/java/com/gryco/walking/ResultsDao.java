package com.gryco.walking;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ResultsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Results results);

    @Query("DELETE FROM results_table")
    void deleteAll();

    @Query("SELECT * from results_table")
    LiveData<List<Results>> getAllResults();

    @Query("UPDATE results_table SET steps_count = :stepscount, points_count = :pointscount WHERE date = :specificdate")
    int updateres(String stepscount, String pointscount, String specificdate);

    @Query("DELETE FROM results_table WHERE date = :specificdate")
    void deletee(String specificdate);

    @Update
    void update(Results results);
}
