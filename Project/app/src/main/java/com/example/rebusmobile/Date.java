package com.example.rebusmobile;

public class Date {
    static String format(int year, int month, int day)
    {
        return String.format("%d.%02d.%02d", year, month, day);
    }
}
