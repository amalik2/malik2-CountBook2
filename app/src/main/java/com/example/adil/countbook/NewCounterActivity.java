package com.example.adil.countbook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This activity is run whenever the user wants to create a new counter.
 * They are able to set the counter name, the initial value, and the comment.
 * The result is returned through an intent, which the MainActivity will pick up.
 */
public class NewCounterActivity extends AppCompatActivity {

    // Intent data identifiers
    public static final String ID_NAME = "Name", ID_VALUE = "Value", ID_DESC = "Desc";

    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_counter);

        errorText = (TextView)findViewById(R.id.invalidNameText);
    }

    /**
     * Event called when the finish button is clicked
     * Ends this activity and prepares the results for MainActivity to handle
     * @param view
     */
    public void onFinishClicked(View view){

        EditText nameText = (EditText) findViewById(R.id.nameEditableText);
        String name = nameText.getText().toString();
        // The name can not be blank
        if (name.length() == 0){
            errorText.setVisibility(View.VISIBLE);
            return;
        }

        Intent intent = new Intent();

        EditText valueText = (EditText) findViewById(R.id.valueEditableText);
        EditText descText = (EditText) findViewById(R.id.descEditableText);

        intent.putExtra(ID_NAME, name);
        intent.putExtra(ID_VALUE, Long.valueOf(valueText.getText().toString()).longValue());
        intent.putExtra(ID_DESC, descText.getText().toString());

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Event called when the cancel button is clicked
     * Ends this activity
     * @param view
     */
    public void onCancelClicked(View view){
        finish();
    }
}
