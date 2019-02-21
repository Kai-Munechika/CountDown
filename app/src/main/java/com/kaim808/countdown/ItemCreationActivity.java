package com.kaim808.countdown;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ItemCreationActivity extends AppCompatActivity {

    private EditText timeField;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_creation);

        initTimeField();
        getSupportActionBar().setTitle(R.string.create_new_counter);
    }

    private void initTimeField() {
        timeField = findViewById(R.id.time_field);
        timeField.setShowSoftInputOnFocus(false);

        timeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    launchTimePicker();
                }
            }
        });

        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTimePicker();
            }
        });
    }

    private void launchTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker;
        timePicker = new TimePickerDialog(ItemCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
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
                timeField.setText(String.format(Locale.US,"%d:%02d %s", selectedHour, selectedMinute, timePeriod));
            }
        }, hour, minute, false);
        timePicker.setTitle("Select Time");
        timePicker.show();
    }

    // clear focus when tapping outside of an EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public void cancelPressed(View view) {
        if (anyEditTextsFilled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Cancel creating this item?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });

            builder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

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
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
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
            editTexts.add((EditText) findViewById(id));
        }

        return editTexts;
    }
}
