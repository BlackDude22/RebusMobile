package com.example.rebusmobile;

import android.util.Log;

public class Journey {
    private Trip forward;
    private Trip backward;

    private Double price;
    private Integer duration;

    public Journey(Trip forward, Trip backward)
    {
        this.forward = forward;
        this.backward = backward;

        double forwardPrice = 0;
        double backwardPrice = 0;

        int forwardDuration = 0;
        int backwardDuration = 0;

        if (forward != null){
            forwardPrice = Double.valueOf(forward.getRoute().getPrice());
            String[] time = forward.getRoute().getTotalTime().split(":");
            forwardDuration = Integer.parseInt(time[1])*60 + Integer.parseInt(time[0])*3600;

        }

        if (backward != null){
            backwardPrice = Double.valueOf(backward.getRoute().getPrice());
            Log.v("TEST", backward.getRoute().getTotalTime());
            String[] time = backward.getRoute().getTotalTime().split(":");
            backwardDuration = Integer.parseInt(time[1])*60 + Integer.parseInt(time[0])*3600;
        }


        this.price = forwardPrice + backwardPrice;
        this.duration = forwardDuration + backwardDuration;
    }

    public Trip getForward() {
        return forward;
    }

    public Trip getBackward() {
        return backward;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getDuration() {
        return duration;
    }
}
