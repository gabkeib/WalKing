package com.gryco.walking;

import android.app.Application;
import androidx.lifecycle.LiveData;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Gabrielius on 2018.11.01.
 */

public class ResultsRepository {

    private ResultsDao mresultsDao;
    private LiveData<List<Results>> allResults;

    public ResultsRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        mresultsDao = appDatabase.resultsDao();
        allResults = mresultsDao.getAllResults();
    }

    public void insert (Results results){
        new InsertResAsyncTask(mresultsDao).execute(results);
    }

    public void update(Results results){
        new UpdateResAsyncTask(mresultsDao).execute(results);
    }

    public void updateres(Results results){
        new UpdateSpecResAsyncTask(mresultsDao).execute();
    }


    public void deleteAll(){
        new DeleteAllResAsyncTask(mresultsDao).execute();
    }

    public LiveData<List<Results>> getAllResults() {
        return allResults;
    }

    private static class InsertResAsyncTask extends AsyncTask<Results, Void, Void>{
        private ResultsDao resultsDao;

        private InsertResAsyncTask(ResultsDao resultsDao){
            this.resultsDao = resultsDao;
        }

        @Override
        protected Void doInBackground(Results... results) {
            resultsDao.insertAll(results[0]);
            return null;
        }
    }

    private static class UpdateResAsyncTask extends AsyncTask<Results, Void, Void>{
        private ResultsDao resultsDao;

        private UpdateResAsyncTask(ResultsDao resultsDao){
            this.resultsDao = resultsDao;
        }

        @Override
        protected Void doInBackground(Results... results) {
            resultsDao.update(results[0]);
            return null;
        }
    }

    private static class DeleteAllResAsyncTask extends AsyncTask<Void, Void, Void>{
        private ResultsDao resultsDao;

        private DeleteAllResAsyncTask(ResultsDao resultsDao){
            this.resultsDao = resultsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            resultsDao.deleteAll();
            return null;
        }
    }

    private static class UpdateSpecResAsyncTask extends AsyncTask<Results, Void, Void>{
        private ResultsDao resultsDao;
        private String steps;
        private String points;
        private String date;

        private UpdateSpecResAsyncTask(ResultsDao resultsDao){
            this.steps = steps;
            this.points = points;
            this.date = date;
            this.resultsDao = resultsDao;
        }

        @Override
        protected Void doInBackground(Results... results) {
            resultsDao.updateres(steps,points,date);
            return null;
        }
    }

    /*public void updateRes(String steps, String points, String date){
        new ResultsAsyncTask(steps,points,date).execute();
    }

    private static class ResultsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ResultsDao resultsDao;

        ResultsAsyncTask(ResultsDao dao) {
            resultsDao = dao;
        }

        //Prevent leak
       // private WeakReference<Activity> weakActivity;
        private String steps;
        private String points;
        private String date;

        public ResultsAsyncTask(String steps, String points, String date) {
           // weakActivity = new WeakReference<>(activity);
            this.steps = steps;
            this.points = points;
            this.date = date;
        }

        @Override
        protected Void doInBackground(Void... params) {
            resultsDao.updateres(steps,points,date);
            return null;
        }

    }*/

}
