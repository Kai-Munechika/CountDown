package com.kaim808.countdown;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class UpdateCounterService extends IntentService {
    public UpdateCounterService() {
        super("UpdateCounterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("UpdateCounterService", "Service running");
    }
}