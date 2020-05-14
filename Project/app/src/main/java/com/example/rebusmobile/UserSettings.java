package com.example.rebusmobile;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSettings {
    private static boolean isDarkTheme = false;
    private static boolean isLoggedIn = false;
    private static String token;
    private static String id;
    private static String username;
    private static String email;
    private static String name;
    private static String lastName;
    private static String phone;
    private static String country;
    private static String city;
    private static String street;
    private static String house;
    private static String balance;
    private static Journey selectedJourney;

    public static void initialize(JSONObject response){
        try {
            updateToken(response);
            JSONObject entities = response.getJSONObject("ResponseBody").getJSONArray("Entities").getJSONObject(0);
            id = entities.getString("id");
            username = entities.getString("loginName");
            email = entities.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void loadPersonalInfo(JSONObject response){
        try {
            updateToken(response);
            JSONObject entities = response.getJSONObject("ResponseBody").getJSONArray("Entities").getJSONObject(0);
            id = entities.getString("userid");
            name = entities.getString("firstname");
            lastName = entities.getString("lastname");
            phone = entities.getString("phonenumber");
            country = entities.getString("country");
            city = entities.getString("city");
            street = entities.getString("street");
            house = entities.getString("house");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void loadBalance(JSONObject response){
        try {
            updateToken(response);
            JSONObject entities = response.getJSONObject("ResponseBody").getJSONArray("Entities").getJSONObject(0);
            balance = entities.getString("balance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void clear(){
        token = null;
        id = null;
        username = null;
        email = "null";
        name = "null";
        lastName = "null";
        balance = "null";
        country = "null";
        city = "null";
        street = "null";
        house = "null";
        phone = "null";
    }

    public static void updateToken(JSONObject response){
        try {
            token = response.getJSONObject("Header").getJSONObject("ResponseToken").getString("Token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean isIsDarkTheme() {
        return isDarkTheme;
    }

    public static void setIsDarkTheme(boolean isDarkTheme) {
        UserSettings.isDarkTheme = isDarkTheme;
    }

    public static String getToken() {
        return token;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSettings.username = username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserSettings.email = email;
    }

    public static String getId() {
        return id;
    }

    public static String getName() {
        return name;
    }

    public static String getLastName() {
        return lastName;
    }

    public static String getPhone() {
        return phone;
    }

    public static String getCountry() {
        return country;
    }

    public static String getCity() {
        return city;
    }

    public static String getStreet() {
        return street;
    }

    public static String getHouse() {
        return house;
    }

    public static String getBalance() {
        return balance;
    }

    public static Journey getSelectedJourney() {
        return selectedJourney;
    }

    public static void setSelectedJourney(Journey selectedJourney) {
        UserSettings.selectedJourney = selectedJourney;
    }

    public static boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        UserSettings.isLoggedIn = isLoggedIn;
    }
}
