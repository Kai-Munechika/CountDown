package com.kaim808.countdown.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.kaim808.countdown.R;
import com.kaim808.countdown.model.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ItemCreationActivity extends AppCompatActivity {

    private EditText timeField;
    private Item item;
    private boolean editingItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_creation);

        Intent intent = getIntent();
        long itemId = intent.getLongExtra(MainActivity.ITEM_ID, -1);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        editingItem = itemId != -1;
        if (editingItem) {
            item = Item.findById(Item.class, itemId);
            actionBar.setTitle(R.string.edit_counter);
            ((EditText) findViewById(R.id.title_field)).setText(item.getTitle());
            ((EditText) findViewById(R.id.start_field)).setText(item.getValueString());
            ((EditText) findViewById(R.id.increment_field)).setText(item.getIncrementString());
            ((EditText) findViewById(R.id.time_field)).setText(String.format("%s %s", item.getFormattedTime(), item.getTimePeriod().name()));
        } else {
            item = new Item();
            actionBar.setTitle(R.string.create_new_counter);
        }


        initTimeField();
    }

    private void initTimeField() {
        timeField = findViewById(R.id.time_field);
        timeField.setShowSoftInputOnFocus(false);

        timeField.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                launchTimePicker();
            }
        });

        timeField.setOnClickListener(view -> launchTimePicker());
    }

    private void launchTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker;
        timePicker = new TimePickerDialog(ItemCreationActivity.this, (timePicker1, selectedHour, selectedMinute) -> {
            String timePeriod;
            if (selectedHour >= 12) {
                timePeriod = "PM";
                if (selectedHour > 12) {
                    selectedHour -= 12;
                }
            } else {
                timePeriod = "AM";
                if (selectedHour == 0) {
                    selectedHour = 12;
                }
            }
            timeField.setText(String.format(Locale.US, "%d:%02d %s", selectedHour, selectedMinute, timePeriod));
            item.setHour(selectedHour);
            item.setMinute(selectedMinute);
            item.setTimePeriod(timePeriod.equals("AM") ? Item.TimePeriod.AM : Item.TimePeriod.PM);
        }, editingItem ? item.get24HourTimeHour() : hour, editingItem ? item.getMinute() : minute, false);
        timePicker.setTitle("Select Time");
        timePicker.show();
    }

    // clear focus when tapping outside of an EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void cancelPressed(View view) {
        if (anyEditTextsFilled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(editingItem ? "Cancel editing this item?" : "Cancel creating this item?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        dialog.cancel();
                        finish();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            finish();
        }
    }

    public void savePressed(View view) {
        if (!allEditTextsFilled()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        } else {
            item.setTitle(((EditText) findViewById(R.id.title_field)).getText().toString());
            item.setActive(true);
            item.setValue(Double.valueOf(((EditText) findViewById(R.id.start_field)).getText().toString()));
            item.setIncrement(Double.valueOf(((EditText) findViewById(R.id.increment_field)).getText().toString()));
            item.save();

            // if we're editing an active counter, we want its previous scheduled alarm to be cancelled
            // the schedule is identified by item.getId(). If we've created a new item, this does nothing
            MainActivity.cancelAlarm(getApplicationContext(), item);

            MainActivity.scheduleAlarm(getApplicationContext(), item);
            String message = String.format("Increment scheduled daily for %s %s", item.getFormattedTime(), item.getTimePeriod().name());
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    private boolean anyEditTextsFilled() {
        for (EditText editText : getEditTexts()) {
            Editable text = editText.getText();
            if (text != null && !text.toString().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean allEditTextsFilled() {
        for (EditText editText : getEditTexts()) {
            Editable text = editText.getText();
            if (text == null || text.toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private List<EditText> getEditTexts() {
        ArrayList<EditText> editTexts = new ArrayList<>();
        for (int id : Arrays.asList(R.id.title_field, R.id.start_field, R.id.increment_field, R.id.time_field)) {
            editTexts.add((findViewById(id)));
        }

        return editTexts;
    }
}
