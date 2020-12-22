package com.gryco.walking;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Database;
import androidx.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //TODO: create AsyncTask for Database
    //TODO: AlarmManager to save data and delete temporary data

    private static final String TAG = "com.gryco.walking";
    static final public String STEPS_COUNT = "steps_count";
    static final public String SCOUNT = "s_count";

    TextView stepcount;
    BroadcastReceiver receiver;
    BroadcastReceiver receiverr;
    ProgressBar progressBar;
    TextView pointsCount;
    Button datapage;
    Button savingstats;
    Button stopCount;
    Button maps;
    TextView left;
    TextView distt;
    TextView disttext;

    public int stepc;
    int scnt;
    int maxAmountofStepsforPoint;
    public int points =0;
    int steplimit;
    public int pointss;

    String datee;
    String lastdate;

    Boolean started;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private ResultsViewModel resultsViewModel;
  //  Calendar midnight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datapage = findViewById(R.id.datapage);
        stopCount = findViewById(R.id.stop_count);
        disttext = findViewById(R.id.distinkm);

        distt = findViewById(R.id.distcount);
        left = findViewById(R.id.left2);


        Intent databuild = new Intent(this, DataBuilder.class);
        //startService(databuild);

        final Intent stepCount = new Intent(this, StepCounter.class);

        Boolean running = isMyServiceRunning(StepCounter.class);
        Log.i(TAG, "onCreate: " + String.valueOf(running));
        if (!running) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                MainActivity.this.startForegroundService(stepCount);
            } else {
                startService(stepCount);
            }
            started = true;
            stopCount.setText(R.string.stop);
        }
        else {
            started = false;
            stopCount.setText(R.string.startt);
        }


        datapage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataTable.class));
            }
        });

        maps = findViewById(R.id.maps);

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
            }
        });

        stopCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started){
                    stopCount(view);
                    stopCount.setText(R.string.startt);
                    started = false;
                }
                else {
                    startagain(view);
                    stopCount.setText(R.string.stop);
                    started = true;
                }

            }
        });

        steplimit = 520;
        maxAmountofStepsforPoint=1000;

        //progressBar.setMax(maxAmountofStepsforPoint);
        stepcount = (TextView) findViewById(R.id.stepcount);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        pointsCount = (TextView) findViewById(R.id.pointscount);
        //savingstats = findViewById(R.id.saving_button) ;

        lastdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences(TAG,Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        ChechSavedInstance();

        //pointsCount.setText(String.valueOf(points));
        //stepcount.setText(String.valueOf(stepc));

        final UserAdapter adapter = new UserAdapter();
        resultsViewModel = ViewModelProviders.of(this).get(ResultsViewModel.class);
        resultsViewModel.getAllResults().observe(this, new Observer<List<Results>>() {
            @Override
            public void onChanged(@Nullable List<Results> results) {
                adapter.setResults(results);
            }
        });

        //stepc=scnt;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                stepc = intent.getIntExtra(StepCounter.STEP_MSQ,0);
                points = intent.getIntExtra(StepCounter.PCOUNT,0);
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                pointss=points;
               /* if (stepc>steplimit){
                    points++;
                    pointsCount.setText(String.valueOf(points));
                    steplimit+=20;
                }*/
               float distance = (float)(stepc*78)/(float)100000;
               String dist = String.format("%.2f",distance);
               Log.i(TAG,String.valueOf(distance));
                Results results = new Results(String.valueOf(stepc), String.valueOf(points), date, dist);
               if(date==lastdate) {
                   resultsViewModel.update(results);
               }
                if (date!=lastdate){
                    resultsViewModel.insert(results);
                }
                pointsCount.setText(String.valueOf(points));
                stepcount.setText(String.valueOf(stepc));
                distt.setText(dist);
                mEditor.putString("Steps",String.valueOf(stepc));
                mEditor.commit();
                mEditor.putString("Points",String.valueOf(points));
                mEditor.commit();
                mEditor.putString("Distanve",dist);
                int curr = stepc%1000;
                progressBar.setProgress(curr);
                left.setText(String.valueOf(1000-curr));
                mEditor.putString("Left",String.valueOf(1000-curr));
                mEditor.commit();
                lastdate=date;
            }
        };



       /* Runnable rr = new Runnable() {
            @Override
            public void run() {
                 db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production").allowMainThreadQueries().build();

            }
        };
        Thread Roomdata = new Thread(rr);
        Roomdata.start();*/


        datee = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //AppDatabase db;
       /* savingstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = datee;
                float distance = (stepc*78)/100000;
                String stc = String.valueOf(stepc);
                String ptc = String.valueOf(points);
                Results results = new Results(stc,ptc,date,String.valueOf(distance));
                resultsViewModel.insert(results);

            }
        });*/
       // startAlarm();


        String date1 = "2018-10-29";
        String date2 = "2018-10-31";



        }

    private void startAlarm(){

        Calendar midnight = Calendar.getInstance();
        midnight.setTimeInMillis(System.currentTimeMillis());
        midnight.set(Calendar.HOUR_OF_DAY,0);
        midnight.set(Calendar.MINUTE,0);
        midnight.set(Calendar.SECOND,0);

        String dat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        long interval = 1000*60*60*24;
        long shortone = 1000*60*5;





        Intent intent = new Intent(this, DataSaver.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), interval, pendingIntent);
    }

    public void stopCount(View v){
        Intent stepCount = new Intent(this, StepCounter.class);
        stopService(stepCount);
    }

    public void startagain(View v){
        Intent stepCount = new Intent(this, StepCounter.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            MainActivity.this.startForegroundService(stepCount);
        }else{
            startService(stepCount);
        }
    }

    private void ChechSavedInstance(){
        String steps = mPreferences.getString("Steps", "0");
        String points = mPreferences.getString("Points", "0");
        String leftt = mPreferences.getString("Left", "0");
        String distance = mPreferences.getString("Distance", "0");
        Log.i(TAG, "ChechSavedInstance: " +  points);
        stepcount.setText(steps);
        pointsCount.setText(points);
        left.setText(leftt);
        distt.setText(distance);
        int sett = Integer.parseInt(leftt);
        progressBar.setProgress(1000-sett);
    }

   /* @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String saved = savedInstanceState.getString("saved_count");
        stepcount.setText(saved);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String save = String.valueOf(stepc);
        outState.putString("saved_count",save);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(StepCounter.STEP_RESULT));
        Boolean running = isMyServiceRunning(StepCounter.class);
        Log.i(TAG, "onCreate: " + String.valueOf(running));
        if (!running) {
            started = true;
            stopCount.setText(R.string.stop);
        }
        else {
            started = false;
            stopCount.setText(R.string.startt);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*SharedPreferences sharedPref = getSharedPreferences(SCOUNT,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
       editor.putInt(STEPS_COUNT,points);
        editor.apply();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean running = isMyServiceRunning(StepCounter.class);
        Log.i(TAG, "onCreate: " + String.valueOf(running));
        if (!running) {
            started = true;
            stopCount.setText(R.string.stop);
        }
        else {
            started = false;
            stopCount.setText(R.string.startt);
        }
      /*  SharedPreferences sharedPref = getSharedPreferences(SCOUNT,Context.MODE_PRIVATE);
        points = sharedPref.getInt(STEPS_COUNT,0);*/
       // stepcount.setText(String.valueOf(stepc));
        //scnt =
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}

