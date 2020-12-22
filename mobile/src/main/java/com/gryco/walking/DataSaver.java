package com.gryco.walking;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

/**
 * Created by Gabrielius on 2018.10.31.
 */

public class DataSaver extends Service {

    public ResultsViewModel resultsViewModel;

    static final public String TAG = "com.gryco.walking";
    BroadcastReceiver receiver;

    public DataSaver(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Saving");
        if (intent!=null) {


        }
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"Stopped");
        super.onDestroy();
    }
}
