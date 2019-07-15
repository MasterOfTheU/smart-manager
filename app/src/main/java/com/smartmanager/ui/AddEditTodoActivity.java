package com.smartmanager.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.smartmanager.R;
import com.smartmanager.util.Converters;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


public class AddEditTodoActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final String EXTRA_ID =
            "com.example.android.smartmanager.ui.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.android.smartmanager.ui.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.android.smartmanager.ui.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.example.android.smartmanager.ui.EXTRA_PRIORITY";
    public static final String EXTRA_CATEGORY =
            "com.example.android.smartmanager.ui.EXTRA_CATEGORY";
    public static final String EXTRA_HAS_REMINDER =
            "com.example.android.smartmanager.ui.EXTRA_HAS_REMINDER";
    public static final String EXTRA_REMINDER_DATE =
            "com.example.android.smartmanager.ui.EXTRA_REMINDER_DATE";

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private NumberPicker mNumberPickerPriority;
    private Spinner mSpinnerCategory;
    private ArrayAdapter<CharSequence> adapter;
    private SwitchCompat mSwitchCompatReminder;
    private EditText mEditTextReminderDate;
    private EditText mEditTextReminderTime;
    private TextView mTextViewLabelPriority;
    private TextView mTextViewLabelCategory;
    private boolean hasReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        mEditTextTitle = findViewById(R.id.edit_text_title);
        mEditTextDescription = findViewById(R.id.edit_text_description);
        mEditTextReminderDate = findViewById(R.id.edit_text_reminder_date);
        mEditTextReminderTime = findViewById(R.id.edit_text_reminder_time);
        mNumberPickerPriority = findViewById(R.id.numberpicker_priority);
        mSwitchCompatReminder = findViewById(R.id.switchcompat_set_reminder);
        mTextViewLabelPriority = findViewById(R.id.label_priority);
        mTextViewLabelCategory = findViewById(R.id.label_category);
        final LinearLayout reminderLayout = findViewById(R.id.reminder_layout);

        mNumberPickerPriority.setMinValue(1);
        mNumberPickerPriority.setMaxValue(3);
        mNumberPickerPriority.setWrapSelectorWheel(false);
        mSpinnerCategory = findViewById(R.id.spinner_category);
        if (mSpinnerCategory != null) {
            adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.category_array,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            mSpinnerCategory.setAdapter(adapter);
            mSpinnerCategory.setOnItemSelectedListener(this);
        }

        mSwitchCompatReminder.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton cSwitch, boolean isChecked) {
                        if (isChecked) {
                            reminderLayout.setVisibility(View.VISIBLE);
                            hasReminder = true;
                        } else {
                            reminderLayout.setVisibility(View.INVISIBLE);
                            hasReminder = false;
                        }
                    }
                });
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        mEditTextReminderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        mEditTextReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit task");
            mEditTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            mEditTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            mNumberPickerPriority.setValue(intent.getIntExtra
                    (EXTRA_PRIORITY, R.integer.priority_default_value));
            String categorySelected = intent.getStringExtra(EXTRA_CATEGORY);
            mSpinnerCategory.setSelection(adapter.getPosition(categorySelected));
            if (intent.hasExtra(EXTRA_REMINDER_DATE) &&
                    intent.getBooleanExtra(EXTRA_HAS_REMINDER, false)) {
                mSwitchCompatReminder.setChecked
                        (intent.getBooleanExtra(EXTRA_HAS_REMINDER, true));
                reminderLayout.setVisibility(View.VISIBLE);
                String dateTimeString = intent.getSerializableExtra(EXTRA_REMINDER_DATE).toString();
                String[] dateTimeValues = Converters.jodaDateTimeToStringArray(dateTimeString);
                mEditTextReminderDate.setText(dateTimeValues[0]);
                mEditTextReminderTime.setText(dateTimeValues[1]);
            }
        } else {
            mNumberPickerPriority.setVisibility(View.INVISIBLE);
            mSpinnerCategory.setVisibility(View.INVISIBLE);
            mTextViewLabelPriority.setVisibility(View.INVISIBLE);
            mTextViewLabelCategory.setVisibility(View.INVISIBLE);
            mSwitchCompatReminder.setChecked
                    (intent.getBooleanExtra(EXTRA_HAS_REMINDER, false));
            setTitle("Add task");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_item) {
            saveTodo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        displayToast("Date set");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        displayToast("Time set");
    }

    /**
     * Saves the data from all input fields and controls and puts it
     * into bundle to send back to the main activity where it can be saved
     * using background async task.
     */
    private void saveTodo() {
        String title = mEditTextTitle.getText().toString();
        String description = mEditTextDescription.getText().toString();
        int priority = mNumberPickerPriority.getValue();
        String category = mSpinnerCategory.getSelectedItem().toString();

        if (title.trim().isEmpty() || priority == 0 || category.trim().isEmpty()) {
            displayToast("Please, fill out the fields and set other values");
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_CATEGORY, category);

        hasReminder = mSwitchCompatReminder.isChecked();

        if (hasReminder) {
            Date reminderDate;
            if (!(mEditTextReminderDate.getText().toString().isEmpty()) &&
                    !(mEditTextReminderTime.getText().toString().isEmpty())) {

                String inputReminderDate = mEditTextReminderDate.getText().toString()
                        + " " + mEditTextReminderTime.getText().toString();

                DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
                reminderDate = dtf.parseDateTime(inputReminderDate).toDate();
                if (reminderDate.before(new Date())) {
                    displayToast("The date you entered is in the past. Pick another date.");
                    return;
                } else {
                    // TODO: Create pending intent with notification
                }
            } else {
                displayToast("Please, pick a date and time to set a reminder!");
                return;
            }

            data.putExtra(EXTRA_HAS_REMINDER, hasReminder);
            data.putExtra(EXTRA_REMINDER_DATE, reminderDate);
        }


        int id = getIntent().getIntExtra(EXTRA_ID, R.integer.no_value_id);
        if (id != R.integer.no_value_id) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Sets the chosen values to the edit text field for further processing.
     *
     * @param year  year that was set
     * @param month month that was set
     * @param day   day that was set
     * @see AddEditTodoActivity#saveTodo()
     */
    public void processDatePickerResult(int year, int month, int day) {
        String year_string = Integer.toString(year);
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String dateString = day_string + "/" +
                month_string + "/" +
                year_string;
        mEditTextReminderDate.setText(dateString);
    }

    /**
     * Sets the chosen values to the edit text field for further processing.
     *
     * @param hourOfDay hour of day that was set
     * @param minute    minute that was set
     * @see AddEditTodoActivity#saveTodo()
     */
    public void processTimePickerResult(int hourOfDay, int minute) {
        StringBuilder timeSb = new StringBuilder();
        if (hourOfDay < 10) {
            timeSb.append(0);
        }
        timeSb.append(hourOfDay);
        timeSb.append(":");
        if (minute < 10) {
            timeSb.append(0);
        }
        timeSb.append(minute);
        String formattedTimeString = timeSb.toString();
        mEditTextReminderTime.setText(formattedTimeString);
    }

    private void showDatePickerDialog() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Displays the actual message in a toast message.
     *
     * @param message Message to display.
     */
    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

}
