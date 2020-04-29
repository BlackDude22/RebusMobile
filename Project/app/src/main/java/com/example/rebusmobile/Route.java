package com.example.rebusmobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Route {
    private String price;
    private String totalTime;
    private int best;
    private ArrayList<Flight> flightList = new ArrayList<>();

    public Route(JSONObject route)
    {
        try {
            price = route.getString("price");
            totalTime = route.getString("timeSpan");
            best = route.getInt("best");
            JSONArray flights = route.getJSONArray("flights");
            for (int i = 0; i < flights.length(); i++)
            {
                flightList.add(new Flight(flights.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPrice() {
        return price;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public int getBest() {
        return best;
    }

    public Flight getRouteStart() {
        return flightList.get(0);
    }

    public Flight getRouteEnd(){
        return flightList.get(flightList.size() - 1);
    }

    public int getStops(){
        return flightList.size();
    }

    public ArrayList<Flight> getFlightList() {
        return flightList;
    }
}
