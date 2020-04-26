package com.example.rebusmobile;

public class Date {
    static public String format(int year, int month, int day)
    {
        return String.format("%d-%02d-%02d", year, month, day);
    }

    static public boolean compare(String first, String second)
    {
        return first.compareTo(second) < 0;
    }
}
