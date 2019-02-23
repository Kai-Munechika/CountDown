package com.kaim808.countdown;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.kaim808.countdown.activities.MainActivity;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.kaim808.countdown.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        long itemId = intent.getLongExtra(MainActivity.ITEM_ID, -1);
        if (itemId == -1) { Log.i("AlarmRelated", "No itemId received"); return; }

        Intent i = new Intent(context, UpdateCounterService.class);
        i.putExtra(MainActivity.ITEM_ID, itemId);
        context.startService(i);

        Log.i("AlarmRelated", "UpdateCounterService triggered");
    }

    // Once you enable the receiver this way, it will stay enabled, even if the user reboots the device.
    public static void enableReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, MyAlarmReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    // This means that the receiver will not be called unless the application explicitly enables it
    // This prevents the boot receiver from being called unnecessarily
    public static void disableReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, MyAlarmReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}