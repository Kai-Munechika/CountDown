package com.kaim808.countdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kaim808.countdown.Model.Item;
import com.kaim808.countdown.Model.TimePeriod;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        items = new ArrayList<>();
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", "12:00", TimePeriod.AM, false, 940, -16));
        ItemAdapter adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
