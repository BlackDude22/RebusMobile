package com.example.rebusmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class SearchResultActivity extends AppCompatActivity {

    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
    private String isOneWay;
    private String onlyDirect;
    private Integer numberOfPassengers;
    private int currentCardCount = 0;
    private int nextCardCount = 5;
    private Button showMoreButton;
    private Button sortByCheapestButton;
    private Button sortByMostExpensiveButton;
    private Button sortByShortestButton;

    ArrayList<Journey> journeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        departureAirport = getIntent().getStringExtra("DEPARTURE_AIRPORT");
        arrivalAirport = getIntent().getStringExtra("ARRIVAL_AIRPORT");
        departureDate = getIntent().getStringExtra("DEPARTURE_DATE");
        arrivalDate = getIntent().getStringExtra("ARRIVAL_DATE");
        isOneWay = getIntent().getStringExtra("IS_ONE_WAY");
        onlyDirect = getIntent().getStringExtra("ONLY_DIRECT");
        RebusNeoConnector connector = RebusNeoConnector.getInstance(this);

        String request = connector.getJourneyRequest(departureAirport, arrivalAirport, departureDate, arrivalDate, isOneWay, onlyDirect);
        Log.v("TEST", request);

        connector.SendRequest(connector.REQUEST_FLIGHTS,request, new IResponseListener() {
            @Override
            public void onResponse(Object response) {
                loadJourneys((JSONObject) response);
                createJourneyCards();
            }
            @Override
            public void onError(String message) {
//                JSONObject response = null;
//                try {
//                    response = new JSONObject("{\"Header\":{\"ResponseError\":{\"ErrorCode\":0,\"ErrorMessage\":\"\"}},\"ResponseBody\":{\"Entities\":[{\"trips\":[{\"tripParams\":{\"origin\":\"CDG\",\"destination\":\"LHR\",\"depDate\":\"2020-1-1\",\"onlyDirect\":false},\"numOfRoutes\":20,\"routes\":[{\"price\":40.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3404\",\"flightCode\":\"BA-1101\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T05:30:00\",\"arrives\":\"2020-01-01T06:20:00\",\"price\":40.0}]},{\"price\":70.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3414\",\"flightCode\":\"BA-1103\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T07:30:00\",\"arrives\":\"2020-01-01T08:20:00\",\"price\":70.0}]},{\"price\":50.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3389\",\"flightCode\":\"BA-1105\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T08:10:00\",\"arrives\":\"2020-01-01T09:00:00\",\"price\":50.0}]},{\"price\":50.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3441\",\"flightCode\":\"BA-1107\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T12:30:00\",\"arrives\":\"2020-01-01T13:20:00\",\"price\":50.0}]},{\"price\":50.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"38998\",\"flightCode\":\"BA-1109\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T17:30:00\",\"arrives\":\"2020-01-01T18:20:00\",\"price\":50.0}]},{\"price\":40.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"39005\",\"flightCode\":\"BA-1111\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T20:30:00\",\"arrives\":\"2020-01-01T21:20:00\",\"price\":40.0}]},{\"price\":40.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3423\",\"flightCode\":\"BA-1113\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T22:30:00\",\"arrives\":\"2020-01-01T23:20:00\",\"price\":40.0}]},{\"price\":50.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3982\",\"flightCode\":\"AF-1150\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T22:00:00\",\"arrives\":\"2020-01-01T22:50:00\",\"price\":50.0}]},{\"price\":30.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3883\",\"flightCode\":\"AF-1140\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T18:00:00\",\"arrives\":\"2020-01-01T18:50:00\",\"price\":30.0}]},{\"price\":50.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3964\",\"flightCode\":\"AF-1130\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T15:00:00\",\"arrives\":\"2020-01-01T15:50:00\",\"price\":50.0}]},{\"price\":30.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3954\",\"flightCode\":\"AF-1120\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T12:00:00\",\"arrives\":\"2020-01-01T12:50:00\",\"price\":30.0}]},{\"price\":50.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3934\",\"flightCode\":\"AF-1100\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T06:00:00\",\"arrives\":\"2020-01-01T06:50:00\",\"price\":50.0}]},{\"price\":30.0,\"timeSpan\":\"00:50:00\",\"flights\":[{\"flightId\":\"3944\",\"flightCode\":\"AF-1110\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T08:00:00\",\"arrives\":\"2020-01-01T08:50:00\",\"price\":30.0}]},{\"price\":80.0,\"timeSpan\":\"-11:30:00\",\"flights\":[{\"flightId\":\"4017\",\"flightCode\":\"AF-1220\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"departs\":\"2020-01-01T11:30:00\",\"arrives\":\"2020-01-01T13:30:00\",\"price\":40.0},{\"flightId\":\"37941\",\"flightCode\":\"SN-1451\",\"fromAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"toAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"departs\":\"2020-01-01T19:00:00\",\"arrives\":\"2020-01-01T19:30:00\",\"price\":20.0},{\"flightId\":\"37837\",\"flightCode\":\"SN-1130\",\"fromAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T23:00:00\",\"arrives\":\"2020-01-01T00:00:00\",\"price\":20.0}]},{\"price\":90.0,\"timeSpan\":\"-09:30:00\",\"flights\":[{\"flightId\":\"3927\",\"flightCode\":\"AF-1210\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"departs\":\"2020-01-01T09:30:00\",\"arrives\":\"2020-01-01T10:30:00\",\"price\":50.0},{\"flightId\":\"37941\",\"flightCode\":\"SN-1451\",\"fromAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"toAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"departs\":\"2020-01-01T19:00:00\",\"arrives\":\"2020-01-01T19:30:00\",\"price\":20.0},{\"flightId\":\"37837\",\"flightCode\":\"SN-1130\",\"fromAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T23:00:00\",\"arrives\":\"2020-01-01T00:00:00\",\"price\":20.0}]},{\"price\":90.0,\"timeSpan\":\"-09:30:00\",\"flights\":[{\"flightId\":\"3927\",\"flightCode\":\"AF-1210\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"departs\":\"2020-01-01T09:30:00\",\"arrives\":\"2020-01-01T10:30:00\",\"price\":50.0},{\"flightId\":\"37921\",\"flightCode\":\"SN-1421\",\"fromAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"toAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"departs\":\"2020-01-01T14:00:00\",\"arrives\":\"2020-01-01T14:30:00\",\"price\":20.0},{\"flightId\":\"37837\",\"flightCode\":\"SN-1130\",\"fromAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T23:00:00\",\"arrives\":\"2020-01-01T00:00:00\",\"price\":20.0}]},{\"price\":90.0,\"timeSpan\":\"-06:30:00\",\"flights\":[{\"flightId\":\"3992\",\"flightCode\":\"AF-1200\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"departs\":\"2020-01-01T06:30:00\",\"arrives\":\"2020-01-01T07:30:00\",\"price\":50.0},{\"flightId\":\"37941\",\"flightCode\":\"SN-1451\",\"fromAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"toAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"departs\":\"2020-01-01T19:00:00\",\"arrives\":\"2020-01-01T19:30:00\",\"price\":20.0},{\"flightId\":\"37837\",\"flightCode\":\"SN-1130\",\"fromAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T23:00:00\",\"arrives\":\"2020-01-01T00:00:00\",\"price\":20.0}]},{\"price\":90.0,\"timeSpan\":\"-06:30:00\",\"flights\":[{\"flightId\":\"3992\",\"flightCode\":\"AF-1200\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"departs\":\"2020-01-01T06:30:00\",\"arrives\":\"2020-01-01T07:30:00\",\"price\":50.0},{\"flightId\":\"37921\",\"flightCode\":\"SN-1421\",\"fromAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"toAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"departs\":\"2020-01-01T14:00:00\",\"arrives\":\"2020-01-01T14:30:00\",\"price\":20.0},{\"flightId\":\"37837\",\"flightCode\":\"SN-1130\",\"fromAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T23:00:00\",\"arrives\":\"2020-01-01T00:00:00\",\"price\":20.0}]},{\"price\":95.0,\"timeSpan\":\"-15:30:00\",\"flights\":[{\"flightId\":\"37741\",\"flightCode\":\"LH-1321\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Frankfurt Airport\",\"type\":\"Airport\",\"code\":\"FRA\",\"cityName\":\"Frankfurt\",\"countryName\":\"DE\"},\"departs\":\"2020-01-01T15:30:00\",\"arrives\":\"2020-01-01T16:50:00\",\"price\":40.0},{\"flightId\":\"38019\",\"flightCode\":\"SN-1721\",\"fromAirport\":{\"fullName\":\"Frankfurt Airport\",\"type\":\"Airport\",\"code\":\"FRA\",\"cityName\":\"Frankfurt\",\"countryName\":\"DE\"},\"toAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"departs\":\"2020-01-01T20:00:00\",\"arrives\":\"2020-01-01T20:50:00\",\"price\":35.0},{\"flightId\":\"37837\",\"flightCode\":\"SN-1130\",\"fromAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T23:00:00\",\"arrives\":\"2020-01-01T00:00:00\",\"price\":20.0}]},{\"price\":95.0,\"timeSpan\":\"-11:30:00\",\"flights\":[{\"flightId\":\"4017\",\"flightCode\":\"AF-1220\",\"fromAirport\":{\"fullName\":\"Charles de Gaulle Airport\",\"type\":\"Airport\",\"code\":\"CDG\",\"cityName\":\"Paris\",\"countryName\":\"FR\"},\"toAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"departs\":\"2020-01-01T11:30:00\",\"arrives\":\"2020-01-01T13:30:00\",\"price\":40.0},{\"flightId\":\"37931\",\"flightCode\":\"SN-1441\",\"fromAirport\":{\"fullName\":\"Amsterdam Airport Schiphol\",\"type\":\"Airport\",\"code\":\"AMS\",\"cityName\":\"Amsterdam\",\"countryName\":\"NL\"},\"toAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"departs\":\"2020-01-01T17:00:00\",\"arrives\":\"2020-01-01T17:30:00\",\"price\":35.0},{\"flightId\":\"37837\",\"flightCode\":\"SN-1130\",\"fromAirport\":{\"fullName\":\"Brussels Airport\",\"type\":\"Airport\",\"code\":\"BRU\",\"cityName\":\"Brussels\",\"countryName\":\"BE\"},\"toAirport\":{\"fullName\":\"Heathrow Airport\",\"type\":\"Airport\",\"code\":\"LHR\",\"cityName\":\"London\",\"countryName\":\"GB\"},\"departs\":\"2020-01-01T23:00:00\",\"arrives\":\"2020-01-01T00:00:00\",\"price\":20.0}]}]},{\"tripParams\":{\"origin\":\"LHR\",\"destination\":\"CDG\",\"depDate\":\"2020-2-1\",\"onlyDirect\":false},\"numOfRoutes\":0,\"routes\":[]}],\"numOfPass\":1,\"passClass\":\"economy\"}]}}");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                loadJourneys(response);
//                createJourneyCards();
            }
        });

        showMoreButton = findViewById(R.id.showMoreButton);
        sortByCheapestButton = findViewById(R.id.sortByCheapest);
        sortByMostExpensiveButton = findViewById(R.id.sortByMostExpensive);
        sortByShortestButton = findViewById(R.id.sortByShortest);

        sortByCheapestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(journeys, new Comparator<Journey>() {
                    public int compare(Journey o1, Journey o2) {
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });
                currentCardCount = 0;
                nextCardCount = 5;
                clearCards();
                createJourneyCards();
            }
        });

        sortByMostExpensiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Collections.sort(journeys, new Comparator<Journey>() {
//                    public int compare(Journey o1, Journey o2) {
//                        return o2.getPrice().compareTo(o1.getPrice());
//                    }
//                });
//                currentCardCount = 0;
//                nextCardCount = 5;
//                clearCards();
//                createJourneyCards();
            }
        });

        sortByShortestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(journeys, new Comparator<Journey>() {
                    public int compare(Journey o1, Journey o2) {
                        return o1.getDuration().compareTo(o2.getDuration());
                    }
                });
                currentCardCount = 0;
                nextCardCount = 5;
                clearCards();
                createJourneyCards();
            }
        });

        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextCardCount += 5;
                createJourneyCards();
            }
        });

    }

    private void loadJourneys(JSONObject response)
    {

        ArrayList<Trip> toTrips = new ArrayList<>();
        ArrayList<Trip> fromTrips = new ArrayList<>();

        JSONArray trips = null;
        JSONArray toRoutesJSON = null;
        JSONObject toParamsJSON = null;
        JSONArray fromRoutesJSON = null;
        JSONObject fromParamsJSON = null;
        Calendar calendar = Calendar.getInstance();
        Log.v("TEST", "Loading JSONS " + calendar.getTimeInMillis());
        try {
            trips = response.getJSONObject("ResponseBody").getJSONArray("Entities").getJSONObject(0).getJSONArray("trips");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            toRoutesJSON = trips.getJSONObject(0).getJSONArray("routes");
            toParamsJSON = trips.getJSONObject(0).getJSONObject("tripParams");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            fromRoutesJSON = trips.getJSONObject(1).getJSONArray("routes");
            fromParamsJSON = trips.getJSONObject(1).getJSONObject("tripParams");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TEST", "Loading routes " + calendar.getTimeInMillis());
        if (toRoutesJSON != null){
            for (int i = 0; i < toRoutesJSON.length(); i++) {
                try {
                    toTrips.add(new Trip(toParamsJSON, toRoutesJSON.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (fromRoutesJSON != null) {
            for (int i = 0; i < fromRoutesJSON.length(); i++) {
                try {
                    fromTrips.add(new Trip(fromParamsJSON, fromRoutesJSON.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.v("TEST", "Loading trips " + calendar.getTimeInMillis());
        for (Trip forward: toTrips)
        {
            if (fromTrips.isEmpty()) {
                journeys.add(new Journey(forward, null));
            } else {
                for (Trip backward: fromTrips)
                    journeys.add(new Journey(forward, backward));
            }
        }

        Log.v("nx", "Journeys: " + journeys.size());
        Log.v("nx", "To Journeys: " + toTrips.size());
        Log.v("nx", "From Journeys: " + fromTrips.size());
    }

    private void createJourneyCards()
    {
        TextView nothingFound = findViewById(R.id.nothingFoundTextView);
        if (journeys.size() == 0){
            nothingFound.setVisibility(View.VISIBLE);
            return;
        } else {
            nothingFound.setVisibility(View.GONE);
        }
        LinearLayout cardContainer = findViewById(R.id.cardContainer);
        for(int i = currentCardCount; i < nextCardCount && i < journeys.size(); i++)
        {
            Journey journey = journeys.get(i);

            CardView card = null;
            if (isOneWay.equals("true")) {
                card = LayoutInflater.from(getApplicationContext()).inflate(R.layout.flight_card_half, null).findViewById(R.id.cardTemplate);
                ((ViewGroup) card.getParent()).removeView(card);
            } else {
                card = LayoutInflater.from(getApplicationContext()).inflate(R.layout.flight_card, null).findViewById(R.id.cardTemplate);
                ((ViewGroup) card.getParent()).removeView(card);
            }
            ((TextView)card.findViewById(R.id.cardToDepartureAirport)).setText(journey.getForward().getRoute().getRouteStart().getDepartureAirport().getName());
            ((TextView)card.findViewById(R.id.cardToArrivalAirport)).setText(journey.getForward().getRoute().getRouteEnd().getArrivalAirport().getName());
            ((TextView)card.findViewById(R.id.cardToDepartureTime)).setText(journey.getForward().getRoute().getRouteStart().getDepartureTime());
            ((TextView)card.findViewById(R.id.cardToArrivalTime)).setText(journey.getForward().getRoute().getRouteEnd().getArrivalTime());
            ((TextView)card.findViewById(R.id.cardToPrice)).setText(journey.getForward().getRoute().getPrice() + "€");
            int stopsTo = journey.getForward().getRoute().getStops();
            String stopsToText = "";
            if (stopsTo == 1)
                stopsToText = "Direct flight";
            else if (stopsTo == 2)
                stopsToText = String.valueOf(stopsTo - 1) + " stop";
            else stopsToText = String.valueOf(stopsTo - 1) + " stops";
            ((TextView)card.findViewById(R.id.cardToStops)).setText(stopsToText);


            if (isOneWay.equals("false")) {
                ((TextView)card.findViewById(R.id.cardBackDepartureAirport)).setText(journey.getBackward().getRoute().getRouteStart().getDepartureAirport().getName());
                ((TextView)card.findViewById(R.id.cardBackArrivalAirport)).setText(journey.getBackward().getRoute().getRouteEnd().getArrivalAirport().getName());
                ((TextView)card.findViewById(R.id.cardBackDepartureTime)).setText(journey.getBackward().getRoute().getRouteStart().getDepartureTime());
                ((TextView)card.findViewById(R.id.cardBackArrivalTime)).setText(journey.getBackward().getRoute().getRouteEnd().getArrivalTime());
                ((TextView)card.findViewById(R.id.cardBackPrice)).setText(journey.getBackward().getRoute().getPrice() + "€");
                int stopsBack = journey.getBackward().getRoute().getStops();
                String stopsBackText = "";
                if (stopsBack == 1)
                    stopsBackText = "Direct flight";
                else if (stopsBack == 2)
                    stopsBackText = String.valueOf(stopsBack - 1) + " stop";
                else stopsBackText = String.valueOf(stopsBack - 1) + " stops";
                ((TextView)card.findViewById(R.id.cardBackStops)).setText(stopsBackText);
            }

            cardContainer.addView(card);
            currentCardCount++;
        }

        if (currentCardCount < journeys.size())
            showMoreButton.setVisibility(View.VISIBLE);
        else showMoreButton.setVisibility(View.GONE);
    }

    private void clearCards(){
        LinearLayout cardContainer = findViewById(R.id.cardContainer);
        ((ViewGroup)cardContainer).removeAllViewsInLayout();
    }
}
