package com.example.adil.countbook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Represents the activity that is started when the user wants to view
 * the details of an existing counter. They are able to edit
 * everything in the counter except for the date.
 * The changeable fields in the counter have their new values returned through an intent,
 * which the MainActivity will pick up and handle.
 * The user can also delete the counter through this activity, which is also returned through the intent.
 */
public class CounterDetailsActivity extends AppCompatActivity {

    // Intent extra data identifier for the counter to view
    public static final String ID_COUNTER = "Counter";
    // Index of the counter being modified for the return result
    public static final String ID_INDEX = "Index";

    // Intent extra data identifier for whether the counter was deleted or not
    public static final String ID_DELETED = "Deleted";
    // CVALUE = Current Value
    public static final String ID_COUNTER_NAME = "Name", ID_COUNTER_VALUE = "Value", ID_COUNTER_DESC = "Desc", ID_COUNTER_CVALUE = "CValue";

    private Counter counter; // the counter to modify
    private int index;

    private EditText nameText;
    private EditText initialValueText;
    private EditText descText;
    private TextView dateText;
    private TextView errorText;
    private EditText currentValueText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_details);

        Intent intent = getIntent();
        counter = (Counter)intent.getSerializableExtra(ID_COUNTER);
        index = intent.getIntExtra(ID_INDEX, 0);

        nameText = (EditText) findViewById(R.id.nameEditText);
        initialValueText = (EditText) findViewById(R.id.valueEditText);
        descText = (EditText) findViewById(R.id.descriptionEditText);
        dateText = (TextView) findViewById(R.id.dateDynamicText);
        errorText = (TextView)findViewById(R.id.detailsInvalidNameText);
        currentValueText = (EditText)findViewById(R.id.currentValueEditableText);
    }

    @Override
    protected void onStart() {
        super.onStart();

        nameText.setText(counter.getName());
        initialValueText.setText(String.valueOf(counter.getInitialValue()));
        descText.setText(counter.getComment());
        dateText.setText(counter.getDate());
        currentValueText.setText(String.valueOf(counter.getCurrentValue()));
    }

    public void onDeleteButtonClicked(View view){
       exitDetails(true);
    }

    public void onFinishButtonClicked(View view){
        // The name can not be blank
        if (nameText.getText().length() == 0){
            errorText.setVisibility(View.VISIBLE);
            return;
        }

        exitDetails(false);
    }

    /**
     * Exit viewing details about the current counter
     * @param deleted is true if the counter was deleted, false if it was not
     */
    private void exitDetails(boolean deleted){
        Intent intent = new Intent();
        intent.putExtra(ID_DELETED, deleted);
        intent.putExtra(ID_COUNTER_NAME, nameText.getText().toString());
        intent.putExtra(ID_COUNTER_VALUE, Long.valueOf(initialValueText.getText().toString()).longValue());
        intent.putExtra(ID_COUNTER_DESC, descText.getText().toString());
        intent.putExtra(ID_COUNTER_CVALUE, Long.valueOf(currentValueText.getText().toString()).longValue());
        intent.putExtra(ID_INDEX, index);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Increase the counter's current value by 1
     * @param view
     */
    public void onIncreaseButtonClicked(View view){
        // The current value EditText was changed, so apply this to the counter
        long textValue = Long.valueOf(currentValueText.getText().toString());
        if (textValue != counter.getCurrentValue())
            counter.setCurrentValue(textValue);
        counter.increment();
        currentValueText.setText(String.valueOf(counter.getCurrentValue()));
    }

    /**
     * Decrease the counter's current value by 1
     * @param view
     */
    public void onDecreaseButtonClicked(View view){
        // The current value EditText was changed, so apply this to the counter
        long textValue = Long.valueOf(currentValueText.getText().toString());
        if (textValue != counter.getCurrentValue())
            counter.setCurrentValue(textValue);
        counter.decrement();
        currentValueText.setText(String.valueOf(counter.getCurrentValue()));
    }

    /**
     * Reset the counter's current value to the initial value
     * @param view
     */
    public void onResetButtonClicked(View view){
        // The initial value EditText was changed, so apply this to the counter
        long textValue = Long.valueOf(initialValueText.getText().toString());
        if (textValue != counter.getInitialValue())
            counter.setInitialValue(textValue);
        counter.resetValue();
        currentValueText.setText(String.valueOf(counter.getCurrentValue()));
    }

}
