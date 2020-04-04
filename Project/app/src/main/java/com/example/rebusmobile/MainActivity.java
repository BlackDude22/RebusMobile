package com.example.rebusmobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Airport> AIRPORTS = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener onDepartureDateSetListener;
    private DatePickerDialog.OnDateSetListener onArrivalDateSetListener;

    private String departureAirport = null;
    private String arrivalAirport = null;
    private String departureDate = null;
    private String arrivalDate = null;
    private Integer numberOfPassengers = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AutoCompleteTextView departureTextView = findViewById(R.id.departureSearchBox);
        final AutoCompleteTextView arrivalTextView = findViewById(R.id.arrivalSearchBox);
        final EditText departureDatePicker = findViewById(R.id.departureDatePicker);
        final EditText arrivalDatePicker = findViewById(R.id.arrivalDatePicker);
        final EditText passengerEditText = findViewById(R.id.passengersEditText);

        RebusNeoConnector connector = RebusNeoConnector.getInstance(this);

        connector.SendRequest(connector.GET_AIRPORTS,null, new IResponseListener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(getApplicationContext() , response.toString(), Toast.LENGTH_LONG);
                loadSearchSuggestions((JSONObject) response);
                bindSearchSuggestions(departureTextView, arrivalTextView);
            }
            @Override
            public void onError(String message) {

            }
        });

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
                departureDate = Date.format(year, month + 1, dayOfMonth);
                departureDatePicker.setText(departureDate);
            }
        };

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
                arrivalDate = Date.format(year, month + 1, dayOfMonth);
                arrivalDatePicker.setText(arrivalDate);
            }
        };

        final Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passengerText = passengerEditText.getText().toString();
                if (!passengerText.isEmpty())
                    numberOfPassengers =  Integer.parseInt(passengerText);

                boolean hasErrors = false;

                if (departureAirport == null) {
                    departureTextView.setError(getString(R.string.departure_airport_missing_error));
                    hasErrors = true;
                }
                if (arrivalAirport == null){
                    arrivalTextView.setError(getString(R.string.arrival_airport_missing_error));
                    hasErrors = true;
                }

                if (departureDate == null){
                    departureDatePicker.setError(getString(R.string.departure_date_missing_error));
                    hasErrors = true;
                }

                if (arrivalDate == null){
                    arrivalDatePicker.setError(getString(R.string.arrival_date_missing_error));
                    hasErrors = true;
                }

                if (numberOfPassengers == null){
                    passengerEditText.setError(getString(R.string.passenger_missing_error));
                    hasErrors = true;
                }

//                if (hasErrors)
//                    return;

                Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("DEPARTURE_AIRPORT", departureAirport);
                intent.putExtra("ARRIVAL_AIRPORT", arrivalAirport);
                intent.putExtra("DEPARTURE_DATE", departureDate);
                intent.putExtra("ARRIVAL_DATE", arrivalDate);
                intent.putExtra("NUMBER_OF_PASSENGERS", numberOfPassengers);
                startActivity(intent);
            }
        });
    }

    private void bindSearchSuggestions(final AutoCompleteTextView departureTextView, final AutoCompleteTextView arrivalTextView)
    {
        AIRPORTS.add(new Airport("AAA", "BBB", "CCC", "DDD"));

        ArrayAdapter<Airport> airportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AIRPORTS);
        departureTextView.setAdapter(airportAdapter);
        arrivalTextView.setAdapter(airportAdapter);

        departureTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Airport airport = (Airport) parent.getItemAtPosition(position);
                departureTextView.setText(airport.getName());
                departureAirport = airport.getCode();
            }
        });

        arrivalTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Airport airport = (Airport) parent.getItemAtPosition(position);
                arrivalTextView.setText(airport.getName());
                arrivalAirport = airport.getCode();
            }
        });
    }

    private void loadSearchSuggestions(JSONObject response)
    {
        try {
            JSONArray airports = response.getJSONObject("ResponseBody").getJSONArray("Entities").getJSONObject(0).getJSONArray("airports");
            JSONArray countries = response.getJSONObject("ResponseBody").getJSONArray("Entities").getJSONObject(0).getJSONArray("countries");

            HashMap<String, String> countryMap = new HashMap<>();

            for (int i = 0; i < countries.length(); i++)
            {
                JSONObject country = countries.getJSONObject(i);
                countryMap.put(country.getString("code"), country.getString("fullName"));
            }
            for (int i = 0; i < airports.length(); i++)
            {
                JSONObject airport = airports.getJSONObject(i);
                AIRPORTS.add( new Airport(airport.getString("code"), airport.getString("fullName"), airport.getString("cityName"), countryMap.get(airport.getString("countryName"))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
