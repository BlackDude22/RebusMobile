package com.example.rebusmobile;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RebusNeoConnector {

    private String url;
    final RequestQueue queue;

    final public String GET_AIRPORTS = "/locations";
    final public String REQUEST_FLIGHTS = "/journey";
    final public String REQUEST_LOGIN = "/login";
    final public String REQUEST_REGISTER = "/register";

    final public int GET = Request.Method.GET;
    final public int POST = Request.Method.POST;

    static RebusNeoConnector instance = null;

    private RebusNeoConnector(Context context){
        url ="http://rebus.sytes.net";
        queue = Volley.newRequestQueue(context);
    }

    public static RebusNeoConnector getInstance(Context context)
    {
        if (instance == null)
            instance = new RebusNeoConnector(context);

        return instance;
    }

    public void SendRequest(int requestMethod, String action, String request, final IResponseListener listener)
    {
        String completeAction = url + action;
        if (request != null)
            completeAction += request;

        Log.v("Connector", completeAction);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (requestMethod, completeAction, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                    }
                }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };

        queue.add(jsonObjectRequest);
    }

    public String getJourneyRequest(String origin, String destination, String depDate, String retDate, String isOneWay, String onlyDirect)
    {
        return "?origin=" + origin + "&destination=" + destination + "&depDate=" + depDate + "&retDate=" + retDate + "&isOneWay=" + isOneWay + "&onlyDirect=" + onlyDirect;
    }

    public String getLoginRequest(String username, String password)
    {
        return "?username=" + username + "&password=" + password;

    }

    public String getRegisterRequest(String username, String password, String email)
    {
        return "?username=" + username + "&email=" + email + "&password=" + password;
    }
}
