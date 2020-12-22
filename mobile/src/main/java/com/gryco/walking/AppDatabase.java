package com.gryco.walking;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;


@Database(entities = {Results.class},version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ResultsDao resultsDao();

    private static AppDatabase INSTANCE;


     public static synchronized  AppDatabase getDatabase(Context context) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database").fallbackToDestructiveMigration()
                            .addCallback(roomcallback)
                            .build();

                }

        return INSTANCE;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private ResultsDao resultsDao;

        private PopulateDbAsyncTask(AppDatabase db){
            resultsDao = db.resultsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
