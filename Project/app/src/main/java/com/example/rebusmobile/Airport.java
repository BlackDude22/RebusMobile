package com.example.rebusmobile;

public class Airport {
    private String code;
    private String name;
    private String city;
    private String country;

    public Airport(String code, String name, String city, String country)
    {
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString()
    {
        return code + ' ' + name + ' ' + city + ' ' + country;
    }

    public String getName()
    {
        return name + " (" + code + ")";
    }
}
