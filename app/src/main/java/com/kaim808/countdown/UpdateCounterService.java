package com.kaim808.countdown;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kaim808.countdown.activities.MainActivity;
import com.kaim808.countdown.model.Item;

public class UpdateCounterService extends IntentService {

    private Handler handler;

    public UpdateCounterService() {
        super("UpdateCounterService");
        handler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long itemId = intent.getLongExtra(MainActivity.ITEM_ID, -1);
        Item item = Item.findById(Item.class, itemId);
        if (item != null) {
            if (item.isActive()) {
                item.setValue(item.getValue() + item.getIncrement());
                item.save();

                Log.i("AlarmRelated", "item incremented");
                sendBroadcast();
                sendNotification(item);
            } else {
                MainActivity.cancelAlarm(getApplicationContext(), item);
            }

        } else {
            Log.i("AlarmRelated", "UpdateCounterService called on deleted item, nothing done");
        }
    }

    private void sendBroadcast() {
        Intent broadcastIntent = new Intent(MainActivity.COUNTER_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        Log.i("AlarmRelated", "Broadcast sent");
    }

    private void sendNotification(Item item) {
        // Configure the channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("myChannelId", "My Channel", importance);
        channel.setDescription("Reminders");

        // disable vibration
        channel.setVibrationPattern(new long[]{ 0 });
        channel.enableVibration(true);

        // Register the channel with the notifications manager
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder mBuilder =
                // Builder class for devices targeting API 26+ requires a channel ID
                new NotificationCompat.Builder(this, "myChannelId")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setVibrate(new long[]{0L})
                        .setAutoCancel(true)
                        .setContentText(String.format("%s counter %s to %s.", item.getTitle(), item.getIncrement() < 0 ? "decremented" : "incremented", String.valueOf(item.getValue())));

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this.getApplicationContext(), MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.getApplicationContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int id = Math.toIntExact(item.getId()); // id allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());
    }
}

