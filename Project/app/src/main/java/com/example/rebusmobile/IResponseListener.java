package com.example.rebusmobile;

public interface IResponseListener {
    void onResponse(Object response);

    void onError(String message);
}
