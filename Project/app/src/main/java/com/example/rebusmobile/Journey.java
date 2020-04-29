package com.example.rebusmobile;

import android.util.Log;

public class Journey {
    private Trip forward;
    private Trip backward;

    private Double price;
    private Integer duration;
    private Integer best;

    public Journey(Trip forward, Trip backward)
    {
        this.forward = forward;
        this.backward = backward;

        double forwardPrice = 0;
        double backwardPrice = 0;

        Integer forwardDuration = 0;
        Integer backwardDuration = 0;

        Integer forwardBest = 0;
        Integer backwardBest = 0;

        if (forward != null){
            forwardPrice = Double.valueOf(forward.getRoute().getPrice());
            String[] time = forward.getRoute().getTotalTime().split(":");
            forwardDuration = Integer.parseInt(time[1])*60 + Integer.parseInt(time[0])*3600;
            forwardBest = forward.getRoute().getBest();
        }

        if (backward != null){
            backwardPrice = Double.valueOf(backward.getRoute().getPrice());
            Log.v("TEST", backward.getRoute().getTotalTime());
            String[] time = backward.getRoute().getTotalTime().split(":");
            backwardDuration = Integer.parseInt(time[1])*60 + Integer.parseInt(time[0])*3600;
            backwardBest = backward.getRoute().getBest();
        }


        this.price = forwardPrice + backwardPrice;
        this.duration = forwardDuration + backwardDuration;
        this.best = forwardBest + backwardBest;
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

    public Integer getBest() {
        return best;
    }
}
