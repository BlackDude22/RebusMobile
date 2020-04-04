package com.example.rebusmobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchResultActivity extends AppCompatActivity {

    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
    private String isOneWay;
    private String onlyDirect;
    private Integer numberOfPassengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        departureAirport = getIntent().getStringExtra("DEPARTURE_AIRPORT");
        arrivalAirport = getIntent().getStringExtra("ARRIVAL_AIRPORT");
        departureDate = getIntent().getStringExtra("DEPARTURE_DATE");
        arrivalDate = getIntent().getStringExtra("ARRIVAL_DATE");
        isOneWay = "true";
        onlyDirect = "true";

        final TextView test = findViewById(R.id.textView);
        RebusNeoConnector connector = RebusNeoConnector.getInstance(this);

        String request = connector.getJourneyRequest(departureAirport, arrivalAirport, departureDate, arrivalDate, isOneWay, onlyDirect);
        Log.v("nx", request);

        connector.SendRequest(connector.REQUEST_FLIGTS,request, new IResponseListener() {
            @Override
            public void onResponse(Object response) {
                test.setText(response.toString());
            }
            @Override
            public void onError(String message) {

            }
        });
    }
}
