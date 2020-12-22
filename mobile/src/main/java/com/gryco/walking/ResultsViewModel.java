package com.gryco.walking;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by Gabrielius on 2018.11.01.
 */

public class ResultsViewModel extends AndroidViewModel {
    private ResultsRepository resultsRepository;
    private LiveData<List<Results>> allResults;

    public ResultsViewModel(@NonNull Application application) {
        super(application);
        resultsRepository = new ResultsRepository(application);
        allResults = resultsRepository.getAllResults();
    }

    public void insert(Results results){
        resultsRepository.insert(results);
    }

    public void update(Results results){
        resultsRepository.update(results);
    }

    public void updateres(Results results){
        resultsRepository.updateres(results);
    }

    public void deleteAll(){
        resultsRepository.deleteAll();
    }

    public LiveData<List<Results>> getAllResults() {
        return allResults;
    }
}
