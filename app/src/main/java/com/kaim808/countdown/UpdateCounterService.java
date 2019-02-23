package com.kaim808.countdown;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.kaim808.countdown.activities.MainActivity;
import com.kaim808.countdown.model.Item;

public class UpdateCounterService extends IntentService {

    private Handler handler;

    public UpdateCounterService() {
        super("UpdateCounterService");
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long itemId = intent.getLongExtra(MainActivity.ITEM_ID, -1);
        Item item = Item.findById(Item.class, itemId);
        if (item != null) {
            item.setValue(item.getValue() + item.getIncrement());
            item.save();

            Log.i("AlarmRelated", "item incremented");
            handler.post(new DisplayToast(this, String.format("%s counter incremented", item.getTitle())));

            System.out.println("intent Received");
            Intent RTReturn = new Intent(MainActivity.COUNTER_BROADCAST);
            LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
            Log.i("AlarmRelated", "Broadcast sent");
        } else {
            Log.i("AlarmRelated", "UpdateCounterService called on deleted item, nothing done");
        }
    }

    class DisplayToast implements Runnable {
        private final Context mContext;
        String mText;

        DisplayToast(Context mContext, String text) {
            this.mContext = mContext;
            mText = text;
        }

        public void run() {
            Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
        }
    }
}

