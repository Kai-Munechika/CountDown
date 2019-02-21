package com.kaim808.countdown;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kaim808.countdown.Model.Item;
import com.kaim808.countdown.Model.TimePeriod;
import com.kaim808.countdown.View.ItemAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        items = new ArrayList<>();
        items.add(new Item("Meal Points", 12, 45, TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", 12, 45, TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", 12, 45, TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", 12, 45, TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", 12, 45, TimePeriod.AM, false, 940, -16));
        items.add(new Item("Meal Points", 12, 45, TimePeriod.AM, false, 940, -16));


        ItemAdapter adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ItemCreationActivity.class));
            }
        });

        setupListDivider();
    }

    private void setupListDivider() {
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
}
