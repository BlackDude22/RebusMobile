package com.example.rebusmobile.ui.search;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.rebusmobile.Airport;
import com.example.rebusmobile.CountryMap;
import com.example.rebusmobile.Date;
import com.example.rebusmobile.IResponseListener;
import com.example.rebusmobile.R;
import com.example.rebusmobile.RebusNeoConnector;
import com.example.rebusmobile.ui.search_result.SearchResultFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;

    private static ArrayList<Airport> AIRPORTS = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener onDepartureDateSetListener;
    private DatePickerDialog.OnDateSetListener onArrivalDateSetListener;

    private String departureAirport = null;
    private String arrivalAirport = null;
    private String departureDate = null;
    private String arrivalDate = null;
    private Integer numberOfPassengers = null;
    private boolean isOneWay = false;
    private boolean allowOnlyDirectFlights = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AutoCompleteTextView departureTextView = getView().findViewById(R.id.departureSearchBox);
        final AutoCompleteTextView arrivalTextView = getView().findViewById(R.id.arrivalSearchBox);
        final EditText departureDatePicker = getView().findViewById(R.id.departureDatePicker);
        final EditText arrivalDatePicker = getView().findViewById(R.id.arrivalDatePicker);
        final EditText passengerEditText = getView().findViewById(R.id.passengersEditText);
        final CheckBox onlyDirectCheckBox = getView().findViewById(R.id.onlyDirectCheckBox);

        final Button submitButton = getView().findViewById(R.id.submitButton);
        final Button clearButton = getView().findViewById(R.id.clearDates);

        if (AIRPORTS.isEmpty()){
            RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());

            connector.SendRequest(connector.GET, connector.GET_AIRPORTS,null, new IResponseListener() {
                @Override
                public void onResponse(Object response) {
                    loadSearchSuggestions((JSONObject) response);
                }
                @Override
                public void onError(String message) {
                    Log.v("TEST", message);
                }
            });
        }

        bindSearchSuggestions(departureTextView, arrivalTextView);

        departureDatePicker.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(calendar.YEAR);
                int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), onDepartureDateSetListener, year, month, day);
//                dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
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

                DatePickerDialog dialog = new DatePickerDialog(getContext(), onArrivalDateSetListener, year, month, day);
//                dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passengerText = passengerEditText.getText().toString();
                allowOnlyDirectFlights = onlyDirectCheckBox.isChecked();
//                isOneWay = !allowReturnFlightsCheckBox.isChecked();
                if (arrivalDate != null)
                    isOneWay = false;
                else isOneWay = true;

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

                if (arrivalDate != null && Date.compare(arrivalDate, departureDate)){
                    arrivalDatePicker.setError(getString(R.string.arrival_date_lower_error));
                    hasErrors = true;
                }

//                if (numberOfPassengers == null){
//                    passengerEditText.setError(getString(R.string.passenger_missing_error));
//                    hasErrors = true;
//                }

                if (hasErrors)
                    return;

                Bundle bundle = new Bundle();
                bundle.putString("DEPARTURE_AIRPORT", departureAirport);
                bundle.putString("ARRIVAL_AIRPORT", arrivalAirport);
                bundle.putString("DEPARTURE_DATE", departureDate);
                bundle.putString("ARRIVAL_DATE", arrivalDate);
                bundle.putString("ONLY_DIRECT", Boolean.toString(allowOnlyDirectFlights));
                bundle.putString("IS_ONE_WAY", Boolean.toString(isOneWay));

                Log.v("nx", Boolean.toString(isOneWay));

                Fragment fragment = new SearchResultFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                departureDatePicker.getText().clear();
                arrivalDatePicker.getText().clear();
                arrivalDate = null;
                departureDate = null;
            }
        });
    }

    private void bindSearchSuggestions(final AutoCompleteTextView departureTextView, final AutoCompleteTextView arrivalTextView)
    {
        ArrayAdapter<Airport> airportAdapter = new ArrayAdapter<Airport>(getContext(), android.R.layout.simple_list_item_1, AIRPORTS);
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