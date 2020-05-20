package com.example.rebusmobile;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserSettings extends AppCompatActivity {
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
    private static ArrayList<SearchItem> previousSearches = new ArrayList<>();
    private static ArrayList<Ticket> tickets = new ArrayList<>();

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

    public static void initializeTickets(JSONObject response){
        try {
            updateToken(response);
            JSONArray entities = response.getJSONObject("ResponseBody").getJSONArray("Entities");
            for (int i = 0; i < entities.length(); i++){
                JSONArray flights = entities.getJSONObject(i).getJSONArray("flights");
                for (int j = 0; j < flights.length(); j++){
                    tickets.add(new Ticket(flights.getString(j)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void loadTickets(){
//        tickets.add(new Ticket());
    }

    public static void loadSearchItems(ArrayList<SearchItem> history){
        if (history != null)
            previousSearches = history;
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

    public static String getEmail() {
        return email;
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

    public static ArrayList<SearchItem> getPreviousSearches() {
        return previousSearches;
    }

    public static void addSearchItem(String departureAirport, String arrivalAirport, String departureDate, String returnDate, Integer passengers, Boolean isOneWay, Boolean allowOnlyDirectFlights){
        previousSearches.add(new SearchItem(departureAirport, arrivalAirport, departureDate, returnDate, passengers, isOneWay, allowOnlyDirectFlights));
    }

    public static ArrayList<Ticket> getTickets() {
        return tickets;
    }
}
