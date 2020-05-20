package com.example.rebusmobile;

import org.json.JSONException;
import org.json.JSONObject;

public class Airport {
    private String code;
    private String name;
    private String city;
    private String country;

    public Airport(JSONObject airport)
    {
        CountryMap countryMap = CountryMap.getInstance();
        try {
            code = airport.getString("code");
            name = airport.getString("fullName");
            city = airport.getString("cityName");
            country = countryMap.get(airport.getString("countryName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Airport(JSONObject airport, Boolean skipCountry)
    {
        try {
            code = airport.getString("code");
            name = airport.getString("fullName");
            city = airport.getString("cityName");
            if (!skipCountry) {
                CountryMap countryMap = CountryMap.getInstance();
                country = countryMap.get(airport.getString("countryName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString()
    {
        return name + " (" + code + "), " + city + ", " + country;
    }

    public String getName()
    {
        return name + " (" + code + ")";
    }

    public String getCode() { return code; }
}
