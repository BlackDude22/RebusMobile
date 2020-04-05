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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

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
                loadSearchSuggestions((JSONObject) response);
                bindSearchSuggestions(departureTextView, arrivalTextView);
            }
            @Override
            public void onError(String message) {
                ((TextView)findViewById(R.id.textViewError)).setText(message);
                JSONObject response = null;
                try {
                    response = new JSONObject("{\"Header\":{\"ResponseError\":{\"ErrorCode\":0,\"ErrorMessage\":\"\"}},\"ResponseBody\":{\"Entities\":[{\"airports\":[{\"fullName\":\"Barcelona El Prat Airport\",\"type\":\"Airport\",\"code\":\"BCN\",\"cityName\":\"Barcelona\",\"countryName\":\"ES\"},{\"fullName\":\"London-Gatwick Airport\",\"type\":\"Airport\",\"code\":\"LGW\",\"cityName\":\"London\",\"countryName\":\"GB\"},{\"fullName\":\"Munich Airport\",\"type\":\"Airport\",\"code\":\"MUC\",\"cityName\":\"Munich\",\"countryName\":\"DE\"},{\"fullName\":\"Leonardo da Vinci-Fiumicino Airport\",\"type\":\"Airport\",\"code\":\"FCO\",\"cityName\":\"Rome\",\"countryName\":\"IT\"},{\"fullName\":\"Sheremetyevo International Airport\",\"type\":\"Airport\",\"code\":\"SVO\",\"cityName\":\"Moscow\",\"countryName\":\"RU\"},{\"fullName\":\"Paris-Orly Airport\",\"type\":\"Airport\",\"code\":\"ORY\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},{\"fullName\":\"Helsinki Airport\",\"type\":\"Airport\",\"code\":\"HEL\",\"cityName\":\"Helsinki\",\"countryName\":\"FI\"},{\"fullName\":\"Luton Airport\",\"type\":\"Airport\",\"code\":\"LTN\",\"cityName\":\"London\",\"countryName\":\"GB\"},{\"fullName\":\"Hartsfield-Jackson Atlanta International Airport\",\"type\":\"Airport\",\"code\":\"ATL\",\"cityName\":\"Atlanta\",\"countryName\":\"US\"},{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},{\"fullName\":\"Frankfurt Airport\",\"type\":\"Airport\",\"code\":\"FRA\",\"cityName\":\"Frankfurt\",\"countryName\":\"DE\"},{\"fullName\":\"Istanbul Ataturk Airport\",\"type\":\"Airport\",\"code\":\"ISL\",\"cityName\":\"Istanbul\",\"countryName\":\"TR\"},{\"fullName\":\"Adolfo Suarez Madrid-Barajas Airport\",\"type\":\"Airport\",\"code\":\"MAD\",\"cityName\":\"Madrid\",\"countryName\":\"ES\"},{\"fullName\":\"Copenhagen Airport\",\"type\":\"Airport\",\"code\":\"CPH\",\"cityName\":\"Copenhagen\",\"countryName\":\"DK\"},{\"fullName\":\"LaGuardia Airport\",\"type\":\"Airport\",\"code\":\"LGA\",\"cityName\":\"New York City\",\"countryName\":\"US\"},{\"fullName\":\"Chicago OHare International Airport\",\"type\":\"Airport\",\"code\":\"ORD\",\"cityName\":\"Chicago\",\"countryName\":\"US\"},{\"fullName\":\"Los Angeles International Airport\",\"type\":\"Airport\",\"code\":\"LAX\",\"cityName\":\"Los Angeles\",\"countryName\":\"US\"},{\"fullName\":\"John F. Kennedy International Airport\",\"type\":\"Airport\",\"code\":\"JFK\",\"cityName\":\"New York City\",\"countryName\":\"US\"},{\"fullName\":\"Denver International Airport\",\"type\":\"Airport\",\"code\":\"DEN\",\"cityName\":\"Denver\",\"countryName\":\"US\"},{\"fullName\":\"Washington Dulles International Airport\",\"type\":\"Airport\",\"code\":\"IAD\",\"cityName\":\"Washington\",\"countryName\":\"US\"},{\"fullName\":\"Toronto Pearson International Airport\",\"type\":\"Airport\",\"code\":\"YYZ\",\"cityName\":\"Toronto\",\"countryName\":\"CA\"},{\"fullName\":\"Beijing Capital International Airport\",\"type\":\"Airport\",\"code\":\"PEK\",\"cityName\":\"Beijing\",\"countryName\":\"CN\"},{\"fullName\":\"Sydney Airport\",\"type\":\"Airport\",\"code\":\"SYD\",\"cityName\":\"Sydney\",\"countryName\":\"AU\"},{\"fullName\":\"Auckland Airport\",\"type\":\"Airport\",\"code\":\"AKL\",\"cityName\":\"Auckland\",\"countryName\":\"NZ\"}],\"countries\":[{\"fullName\":\"United Kingdom\",\"type\":\"Country\",\"code\":\"GB\",\"flag\":\"\"},{\"fullName\":\"France\",\"type\":\"Country\",\"code\":\"FR\",\"flag\":\"\"},{\"fullName\":\"Netherlands\",\"type\":\"Country\",\"code\":\"NL\",\"flag\":\"\"},{\"fullName\":\"Germany\",\"type\":\"Country\",\"code\":\"DE\",\"flag\":\"\"},{\"fullName\":\"Turkey\",\"type\":\"Country\",\"code\":\"TR\",\"flag\":\"\"},{\"fullName\":\"Spain\",\"type\":\"Country\",\"code\":\"ES\",\"flag\":\"\"},{\"fullName\":\"Italy\",\"type\":\"Country\",\"code\":\"IT\",\"flag\":\"\"},{\"fullName\":\"Russia\",\"type\":\"Country\",\"code\":\"RU\",\"flag\":\"\"},{\"fullName\":\"Denmark\",\"type\":\"Country\",\"code\":\"DK\",\"flag\":\"\"},{\"fullName\":\"Belgium\",\"type\":\"Country\",\"code\":\"BE\",\"flag\":\"\"},{\"fullName\":\"Finland\",\"type\":\"Country\",\"code\":\"FI\",\"flag\":\"\"},{\"fullName\":\"USA\",\"type\":\"Country\",\"code\":\"US\",\"flag\":\"\"},{\"fullName\":\"Canada\",\"type\":\"Country\",\"code\":\"CA\",\"flag\":\"\"},{\"fullName\":\"China\",\"type\":\"Country\",\"code\":\"CN\",\"flag\":\"\"},{\"fullName\":\"Australia\",\"type\":\"Country\",\"code\":\"AU\",\"flag\":\"\"},{\"fullName\":\"New Zeland\",\"type\":\"Country\",\"code\":\"NZ\",\"flag\":\"\"}]}]}}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadSearchSuggestions(response);
                bindSearchSuggestions(departureTextView, arrivalTextView);
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

//                if (numberOfPassengers == null){
//                    passengerEditText.setError(getString(R.string.passenger_missing_error));
//                    hasErrors = true;
//                }

                if (hasErrors)
                    return;

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
//        AIRPORTS.add(new Airport("AAA", "BBB", "CCC", "DDD"));

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

            CountryMap countryMap = CountryMap.getInstance();
            countryMap.load(countries);

            for (int i = 0; i < airports.length(); i++)
            {
                JSONObject airport = airports.getJSONObject(i);
                AIRPORTS.add( new Airport(airport));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
