package com.example.rebusmobile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Airport> AIRPORTS = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener onDepartureDateSetListener;
    private DatePickerDialog.OnDateSetListener onArrivalDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RebusNeoConnector connector = RebusNeoConnector.getInstance(this);

        connector.SendRequest(null, new IResponseListener() {
            @Override
            public void onResponse(Object response) {
                initSearchSuggestions((JSONObject) response);
            }
            @Override
            public void onError(String message) {

            }
        });

        final EditText departureDatePicker = findViewById(R.id.departureDatePicker);
        departureDatePicker.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(calendar.YEAR);
                int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, onDepartureDateSetListener, year, month, day);
                dialog.show();
            }
        });

        onDepartureDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                departureDatePicker.setText(Date.format(year, month + 1, dayOfMonth));
            }
        };

        final EditText arrivalDatePicker = findViewById(R.id.arrivalDatePicker);
        arrivalDatePicker.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(calendar.YEAR);
                int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, onArrivalDateSetListener, year, month, day);
                dialog.show();
            }
        });

        onArrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                arrivalDatePicker.setText(Date.format(year, month + 1, dayOfMonth));
            }
        };

    }

    private void initSearchSuggestions(JSONObject airports)
    {
        AIRPORTS.add(new Airport("AAA", "BBB", "CCC", "DDD"));

        final AutoCompleteTextView departureTextView = findViewById(R.id.departureSearchBox);
        final AutoCompleteTextView arrivalTextView = findViewById(R.id.arrivalSearchBox);
        ArrayAdapter<Airport> airportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AIRPORTS);
        departureTextView.setAdapter(airportAdapter);
        arrivalTextView.setAdapter(airportAdapter);

        departureTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Airport airport = AIRPORTS.get(position);
                String result = airport.getName();
                departureTextView.setText(result);
            }
        });

        arrivalTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Airport airport = AIRPORTS.get(position);
                String result = airport.getName();
                arrivalTextView.setText(result);
            }
        });
    }
}
