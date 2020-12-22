package com.gryco.walking;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StepCounter extends Service {

    private static final String TAG = "com.gryco.walking";
    static final public String STEP_RESULT = "com.gryco.walking.StepCounter.SEND_STEPS";
    static final public String STEP_RESUL = "com.gryco.walking.StepCounter.SEND_STEPS";
    static final public String STEP_MSQ = "com.gryco.walking.StepCounter.SEND_MSQ";
    static final public String STEPS_COUNT = "steps_count";
    static final public String SCOUNT = "s_count";
    static final public String SLAST ="stepslimit_last";
    static final public String PCOUNT = "points_count";
    static final public String PAMOUNT = "points_amount";
    static final public String STP = "ss_count";
    static final public String PTP = "pp_count";
    static final public String STEPS_LIMIT = "s_limit";
    static final public String LAST_DAY = "l_day";


    private SensorManager sensorManager;
    private Sensor sensor;
    public int stepc;
    public int mCounterSteps;
    public int mSteps;
    public int mPreviousCounterSteps=0;
   //public int mPreviousPointsCount=0;
    public int Stepslimit;
    public int pointsCount;

    public static AppDatabase db;

    String currDay;
    String lastDay;

    ResultsRepository resultsRepository;


    public StepCounter() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"service started"+mSteps);

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_02";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Walking",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Walking is now running")
                    .setContentText("To discard this message, close the app").build();

            startForeground(1, notification);
        }

        String defaultday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences shareSteps = getSharedPreferences(STEPS_COUNT, Context.MODE_PRIVATE);
        mPreviousCounterSteps = shareSteps.getInt(SCOUNT,0);
        pointsCount = shareSteps.getInt(PAMOUNT,0);
        Stepslimit = shareSteps.getInt(SLAST,0);
        lastDay = shareSteps.getString(LAST_DAY,defaultday);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mCounterSteps=0;

        //resultsRepository = new ResultsRepository(getApplicationContext());
       /* SharedPreferences shareSteps = getSharedPreferences(STEPS_COUNT, Context.MODE_PRIVATE);
        mSteps = shareSteps.getInt(SCOUNT,20);*/
        Runnable r = new Runnable() {
            @Override
            public void run() {
              sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL,5000000);
            }
        };

        Thread StepTread = new Thread(r);
        StepTread.start();
        /*Runnable rr = new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production").allowMainThreadQueries().build();

            }
        };*/
        mSteps+=mPreviousCounterSteps;
       // startAlarm();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       SharedPreferences shareSteps = getSharedPreferences(STEPS_COUNT, Context.MODE_PRIVATE);
        String defaultday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        mPreviousCounterSteps = shareSteps.getInt(SCOUNT,0);
        pointsCount = shareSteps.getInt(PAMOUNT,0);
        Stepslimit = shareSteps.getInt(SLAST,0);
        lastDay = shareSteps.getString(LAST_DAY,defaultday);

        Log.i(TAG,"OnCreate "+mSteps);

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.i(TAG,"OnSensorChanged "+mCounterSteps+" "+mSteps + " laststepsafterturninoff: " + mPreviousCounterSteps + "StepsLimit" + Stepslimit);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Log.i(TAG,date+" "+lastDay );
            if (date.equals(lastDay)) {
                Log.i(TAG,"Equal");
            }
            else {

                mPreviousCounterSteps = 0;
                mSteps=0;
                Stepslimit=0;
                mCounterSteps=0;
            }

            if (mCounterSteps<1){
                mCounterSteps = (int) event.values[0]; //saves last steps count;
            }
            mSteps = (int) event.values[0] - mCounterSteps; //counts steps;
            mSteps = mSteps + mPreviousCounterSteps;
            if (mSteps>Stepslimit){
                pointscounter();
            }
            Log.i(TAG,"Service works i think "+mSteps + " laststepsafterturninoff: " + mPreviousCounterSteps);
            if (currDay!=null&&String.valueOf(mSteps)!=null&&String.valueOf(pointsCount)!=null) {
                Log.i(TAG,"CHECK" + mSteps + " " + pointsCount + " " + currDay);
              // DataBuilder.db.resultsDao().updateres(String.valueOf(mSteps), String.valueOf(pointsCount), dat);
               // resultsRepository.updateRes(String.valueOf(mSteps), String.valueOf(pointsCount), dat);

            }
            lastDay = date;
            sendBroadcastMessage(mSteps,pointsCount);
            SharedPreferences shareSteps = getSharedPreferences(STEPS_COUNT, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shareSteps.edit();
            editor.putInt(SCOUNT,mSteps);
            editor.putInt(PAMOUNT,pointsCount);
            editor.putString(LAST_DAY,lastDay);
            editor.apply();
            //stepcount.setText(String.valueOf(stepc));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void sendBroadcastMessage(int value, int points) {
        Intent inten = new Intent(STEP_RESULT);
        if (value != 0){
            inten.putExtra(STEP_MSQ,value);
            inten.putExtra(PCOUNT,points);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(inten);
    }


    public void pointscounter(){
        pointsCount++;
        Stepslimit+=1000;
        SharedPreferences shareSteps = getSharedPreferences(STEPS_COUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareSteps.edit();
        editor.putInt(SLAST,Stepslimit);
        editor.apply();
    }

    @Override
    public void onDestroy() {

        Log.i(TAG,"Steps saved"+stepc);
       // sensorManager.unregisterListener(sensorEventListener);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    public void updateData (int value, int points, String dat){

       /* new AsyncTask<Void,Void,Void>(){

            privat

            @Override
            protected Void doInBackground(Void... voids) {
                db.resultsDao().updateres(String.valueOf(mSteps), String.valueOf(pointsCount), dat);
                return null;
            }
        };*/
    }

   /* private static class ResultsAsyncTask extends AsyncTask<Void, Void, Void> {

        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private String steps;
        private String points;
        private String date;

        public ResultsAsyncTask(Activity activity, String steps, String points, String date) {
            weakActivity = new WeakReference<>(activity);
            this.steps = steps;
            this.points = points;
            this.date = date;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ResultsDao resultsDao = AppDatabase.getDatabase();
            resultsDao.updateres(steps,points,date);
            return null;
        }

    }*/

    private void startAlarm(){

        Calendar midnight = Calendar.getInstance();
        midnight.setTimeInMillis(System.currentTimeMillis());
        midnight.set(Calendar.HOUR_OF_DAY,17);
        midnight.set(Calendar.MINUTE,37);
        midnight.set(Calendar.SECOND,0);

        String dat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());






        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        Log.i(TAG,"Ss "+ mSteps + " pp " + pointsCount);
        /* intent.putExtra(STP,String.valueOf(mSteps));
         intent.putExtra(PTP,String.valueOf(pointsCount));*/
        intent.putExtra("Date",dat);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
    }


    //@Override
   /* public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }*/
}
