package com.gryco.walking;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlertReceiver extends BroadcastReceiver {
    public static AppDatabase db;
    BroadcastReceiver receiverr;
    StepCounter stepCounter;
    private static final String TAG = "com.gryco.walking";

   public int step;
    public int point;
    @Override
    public void onReceive(Context context, Intent intent) {
        /*step = intent.getIntExtra(StepCounter.STEP_MSQ,0);
        point = intent.getIntExtra(StepCounter.PCOUNT,0);
        String date = intent.getStringExtra("Date");

        String stepc = intent.getStringExtra(StepCounter.STP);
        String points = intent.getStringExtra(StepCounter.PTP);

        Log.i(TAG,"stepc" + stepc + " point" + points);
      // MainActivity.db.resultsDao().insertAll(new Results(String.valueOf(step),String.valueOf(point),date));*/
       //MainActivity.db.resultsDao().insertAll(new Results(stepc, points, date));
        /*String steps  = intent.getStringExtra(StepCounter.SCOUNT);
       String points = intent.getStringExtra(StepCounter.PCOUNT);
       String datee = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
       AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production").build();*/
    }


}
