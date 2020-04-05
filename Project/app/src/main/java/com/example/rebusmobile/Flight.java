package com.example.rebusmobile;

import org.json.JSONException;
import org.json.JSONObject;

public class Flight {
    private String ID;
    private String code;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private String departureTime;
    private String arrivalTime;
    private String price;

    public Flight(JSONObject flight)
    {
        try {
            ID = flight.getString("flightId");
            code = flight.getString("flightCode");
            departureAirport = new Airport(flight.getJSONObject("fromAirport"));
            arrivalAirport = new Airport(flight.getJSONObject("toAirport"));
            departureTime = flight.getString("departs");
            arrivalTime = flight.getString("arrives");
            price = flight.getString("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }
}
