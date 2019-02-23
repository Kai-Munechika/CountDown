package com.kaim808.countdown;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kaim808.countdown.activities.MainActivity;
import com.kaim808.countdown.model.Item;

public class UpdateCounterService extends IntentService {
    public UpdateCounterService() {
        super("UpdateCounterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long itemId = intent.getLongExtra(MainActivity.ITEM_ID, -1);
        Item item = Item.findById(Item.class, itemId);
        if (item != null) {
            item.setValue(item.getValue() + item.getIncrement());
            item.save();

            Log.i("AlarmRelated", "item incremented");
        } else {
            Log.i("AlarmRelated", "UpdateCounterService called on deleted item, nothin done");
        }
    }
}