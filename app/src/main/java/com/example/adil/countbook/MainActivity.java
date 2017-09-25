package com.example.adil.countbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The main activity of the application.
 * Keeps track of all counters the user has created.
 * It also passes information to other activities and handles their results.
 */
public class MainActivity extends AppCompatActivity {

    // Codes to keep track of other activities
    // NEW_REQUEST_CODE = NewCounterActivity, DETAILS_CODE = CounterDetailsActivity
    private static final int NEW_REQUEST_CODE = 1, DETAILS_CODE = 2;

    // The file where counters will be stored
    private static final String COUNTERS_FILE = "counters.sav";

    // A list of all counter objects used for serialization
    private List<Counter> counters = new ArrayList<>();
    private ListView countersList;
    // Adapter used to update countersList
    private ArrayAdapter<Counter> counterArrayAdapter;

    // The text displaying the total number of counters
    private TextView countersAmountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadCounters();
        countersList = (ListView)findViewById(R.id.countersList);
        countersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CounterDetailsActivity.class);
                intent.putExtra(CounterDetailsActivity.ID_COUNTER, counters.get(position));
                intent.putExtra(CounterDetailsActivity.ID_INDEX, position);
                startActivityForResult(intent, DETAILS_CODE);
            }
        });

        countersAmountText = (TextView)findViewById(R.id.amountDynamicText);
        countersAmountText.setText(String.valueOf(counters.size()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        counterArrayAdapter = new ArrayAdapter<Counter>(this,
                R.layout.list_item, counters);
        countersList.setAdapter(counterArrayAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Event called when the add new counter button is clicked
     * Starts a new activity where the user will be able to create a new counter
     * @param view
     */
    public void addCounterClicked(View view){
        Intent intent = new Intent(this, NewCounterActivity.class);
        startActivityForResult(intent, NEW_REQUEST_CODE);
    }

    /**
     * Load all counters from the file containing them
     */
    private void loadCounters(){
        try {
            FileInputStream is = new FileInputStream(new File(this.getFilesDir(), COUNTERS_FILE));
            ObjectInputStream objectInputStream = new ObjectInputStream(is);
            counters = (ArrayList<Counter>)objectInputStream.readObject();
            objectInputStream.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Save all counters to a file containing them
     */
    private void saveCounters(){
        try {
            FileOutputStream os = new FileOutputStream(new File(this.getFilesDir(), COUNTERS_FILE));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
            objectOutputStream.writeObject(counters);
            objectOutputStream.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // When a new counter is created
        if (requestCode == NEW_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                String counterName = data.getStringExtra(NewCounterActivity.ID_NAME);
                long value = data.getLongExtra(NewCounterActivity.ID_VALUE, 0);
                String description = data.getStringExtra(NewCounterActivity.ID_DESC);

                Counter counter = new Counter(counterName, value, description);
                counters.add(counter);
                counterArrayAdapter.notifyDataSetChanged();
                saveCounters();
                countersAmountText.setText(String.valueOf(counters.size()));

            }

            // When the details of an existing counter are being viewed
        } else if (requestCode == DETAILS_CODE){
            if (resultCode == RESULT_OK){
                returnFromDetails(data);
            }
        }
    }

    /**
     * Call this after successfully returning from viewing details about a counter
     * @param data is the activity's results
     */
    private void returnFromDetails(Intent data){
        // whether the counter was deleted or not
        boolean deleted = data.getBooleanExtra(CounterDetailsActivity.ID_DELETED, false);

        String name = data.getStringExtra(CounterDetailsActivity.ID_COUNTER_NAME);
        int index = data.getIntExtra(CounterDetailsActivity.ID_INDEX, 0);

        // The counter that was being viewed
        Counter counter = counters.get(index);

        if (deleted){
            counters.remove(counter);
            countersAmountText.setText(String.valueOf(counters.size()));
        } else {
            long value = data.getLongExtra(CounterDetailsActivity.ID_COUNTER_VALUE, 0);
            String desc = data.getStringExtra(CounterDetailsActivity.ID_COUNTER_DESC);
            long currentValue = data.getLongExtra(CounterDetailsActivity.ID_COUNTER_CVALUE, 0);

            if (counter.getInitialValue() != value)
                counter.setInitialValue(value);
            if (counter.getCurrentValue() != currentValue)
                counter.setCurrentValue(currentValue);
            counter.setName(name);
            counter.setComment(desc);
        }
        counterArrayAdapter.notifyDataSetChanged();
        saveCounters();
    }

}
