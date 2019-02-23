package com.kaim808.countdown.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.kaim808.countdown.MyAlarmReceiver;
import com.kaim808.countdown.R;
import com.kaim808.countdown.model.Item;
import com.kaim808.countdown.view.ItemAdapter;
import com.kaim808.countdown.view.SwipeToDeleteCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String COUNTER_BROADCAST = "COUNTER_BROADCAST";
    public static String ITEM_ID = "ITEM_ID";

    private SwipeRefreshLayout swipeContainer;
    List<Item> items;
    RecyclerView recyclerView;
    ItemAdapter adapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(COUNTER_BROADCAST)) {
                if (adapter != null) {
                    adapter.refresh();
                }
            }
        }
    };
    LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSwipeContainer();
        initRecyclerView();
        initListDividers();
        initFab();

        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(COUNTER_BROADCAST);
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refresh();
    }

    @Override
    protected void onPause() {
        if (adapter.recentlyDeletedItem != null) {
            adapter.recentlyDeletedItem.delete();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void initSwipeContainer() {
        swipeContainer = findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.refresh();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void initRecyclerView() {
        items = new ArrayList<>();
        adapter = new ItemAdapter(this, items);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initListDividers() {
        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = this.obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.divider_margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        ((RecyclerView) findViewById(R.id.recyclerView)).addItemDecoration(itemDecoration);
    }


    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> gotoItemCreation());
    }

    public void gotoItemCreation() {
        startActivity(new Intent(MainActivity.this, ItemCreationActivity.class));
    }

    public static void scheduleAlarm(Context context, Item item) {
        // if this is the first item requiring an alarm
        if (Item.numItemsActive() == 1) {
            MyAlarmReceiver.enableReceiver(context);
            Log.i("AlarmRelated", "receiver enabled");
        }

        int hour = item.get24HourTimeHour();
        int minute = item.getMinute();

        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(context, MyAlarmReceiver.class);
        long itemId = item.getId();
        intent.putExtra(ITEM_ID, itemId);

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, Math.toIntExact(itemId),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // if the input hour and minute is before the current time, move the calendar 1 day forward
        if (hour < calendar.get(Calendar.HOUR_OF_DAY)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        } else if (hour == calendar.get(Calendar.HOUR_OF_DAY) && minute < calendar.get(Calendar.MINUTE)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pIntent);

        Log.i("AlarmRelated", "Alarm scheduled for " + item.getFormattedTime() + " " + item.getTimePeriod().name());
    }

    public static void cancelAlarm(Context context, Item item) {
        // if this is the last active item being switched off
        if (Item.numItemsActive() == 0) {
            MyAlarmReceiver.disableReceiver(context);
            Log.i("AlarmRelated", "receiver disabled");
        }

        Intent intent = new Intent(context, MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, Math.toIntExact(item.getId()),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);

        Log.i("AlarmRelated", "counter " + item.getTitle() +  " cancelled");
    }
}
