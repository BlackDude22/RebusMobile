package com.example.rebusmobile;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchItem {
    private String departureAirport = null;
    private String arrivalAirport = null;
    private String departureDate = null;
    private String returnDate = null;
    private Integer numberOfPassengers = null;
    private boolean isOneWay = false;
    private boolean allowOnlyDirectFlights = false;

    public SearchItem(JSONObject item){
        try {
            departureAirport = item.getString("departureAirport");
            arrivalAirport = item.getString("arrivalAirport");
            departureDate = item.getString("departureDate");
            returnDate = item.getString("arrivalDate");
            numberOfPassengers = item.getInt("passengers");
            isOneWay = item.getBoolean("isOneWay");
            allowOnlyDirectFlights = item.getBoolean("onlyDirect");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public SearchItem(String departureAirport, String arrivalAirport, String departureDate, String returnDate, Integer passengers, Boolean isOneWay, Boolean allowOnlyDirectFlights){
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.numberOfPassengers = passengers;
        this.isOneWay = isOneWay;
        this.allowOnlyDirectFlights = allowOnlyDirectFlights;

    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public Integer getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public boolean allowOnlyDirectFlights() {
        return allowOnlyDirectFlights;
    }
}
