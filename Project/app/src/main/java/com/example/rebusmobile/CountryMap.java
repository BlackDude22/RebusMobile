package com.example.rebusmobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CountryMap {
    private HashMap<String, String> countryMap = new HashMap<>();
    private static final CountryMap instance = new CountryMap();

    public static CountryMap getInstance() {
        return instance;
    }

    private CountryMap() {
    }

    public void load(JSONArray countries)
    {
        for (int i = 0; i < countries.length(); i++)
        {
            try {
                JSONObject country = countries.getJSONObject(i);
                countryMap.put(country.getString("code"), country.getString("fullName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String get(String code)
    {
        return countryMap.get(code);
    }
}
