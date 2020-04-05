package com.example.rebusmobile;

public class Journey {
    private Trip forward;
    private Trip backward;

    public Journey(Trip forward, Trip backward)
    {
        this.forward = forward;
        this.backward = backward;
    }

    public Trip getForward() {
        return forward;
    }

    public Trip getBackward() {
        return backward;
    }
}
