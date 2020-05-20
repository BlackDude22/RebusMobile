package com.example.rebusmobile;

import org.json.JSONException;
import org.json.JSONObject;

public class Ticket {
    private String id;
    private String code;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private String departureTime;
    private String arrivalTime;
    private String price;

    public Ticket(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void loadInfo(JSONObject response){
        try {
            JSONObject flight = response.getJSONObject("ResponseBody").getJSONArray("Entities").getJSONObject(0);

            code = flight.getString("flightCode");
            departureAirport = new Airport(flight.getJSONObject("fromAirport"), true);
            arrivalAirport = new Airport(flight.getJSONObject("toAirport"), true);
            departureTime = flight.getString("departs").replace('T', ' ');
            arrivalTime = flight.getString("arrives").replace('T', ' ');
            price = flight.getString("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCode() {
        return code;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getPrice() {
        return price;
    }
}
