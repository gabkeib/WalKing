package com.gryco.walking;

import android.app.Service;
import androidx.room.Room;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DataBuilder extends Service {

    public static AppDatabase db;
    static final public String TAG = "com.gryco.walking";

    public DataBuilder() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Started");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
