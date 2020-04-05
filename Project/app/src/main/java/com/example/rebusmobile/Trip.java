package com.example.rebusmobile;

import org.json.JSONException;
import org.json.JSONObject;

public class Trip {
    private String origin;
    private String destination;
    private String departureDate;
    private boolean isDirect;
    private Route route;

    public Trip(JSONObject tripParameters, JSONObject route)
    {
        try {
            origin = tripParameters.getString("origin");
            destination = tripParameters.getString("destination");
            departureDate = tripParameters.getString("depDate");
            isDirect = Boolean.parseBoolean(tripParameters.getString("onlyDirect"));
            this.route = new Route(route);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public Route getRoute() {
        return route;
    }
}
