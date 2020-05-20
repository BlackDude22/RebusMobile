package com.example.rebusmobile.ui.tickets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rebusmobile.IResponseListener;
import com.example.rebusmobile.R;
import com.example.rebusmobile.RebusNeoConnector;
import com.example.rebusmobile.Ticket;
import com.example.rebusmobile.UserSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TicketsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());
        connector.sendRequest(connector.GET, connector.REQUEST_ORDERS, connector.getOrdersRequest(UserSettings.getToken(), UserSettings.getId()), new IResponseListener() {
            @Override
            public void onResponse(Object response) {
                try{
                    JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                    int errorCode = responseError.getInt("ErrorCode");
                    String errorMessage = responseError.getString("ErrorMessage");
                    if (errorCode == 0){
                        UserSettings.initializeTickets((JSONObject) response);
                        loadTicketCards();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });

//        UserSettings.loadTickets();
//        loadTicketCards();
    }

    private void loadTicketCards(){
        ArrayList<Ticket> tickets = UserSettings.getTickets();
        final LinearLayout cardContainer = getView().findViewById(R.id.cardContainerTickets);
        if(tickets.isEmpty())
            getActivity().findViewById(R.id.nothingFoundTicketsTextView).setVisibility(View.VISIBLE);
        else getActivity().findViewById(R.id.nothingFoundTicketsTextView).setVisibility(View.GONE);

        RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());

        for (final Ticket item : tickets){
            connector.sendRequest(connector.GET, connector.REQUEST_FLIGHT_INFO, connector.getFlightInfoRequest(item.getId()), new IResponseListener() {
                @Override
                public void onResponse(Object response) {
                    try{
                        JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                        int errorCode = responseError.getInt("ErrorCode");
                        String errorMessage = responseError.getString("ErrorMessage");
                        if (errorCode == 0){
                            item.loadInfo((JSONObject) response);
                            View card = LayoutInflater.from(getActivity()).inflate(R.layout.ticket_card, null);
                            ((TextView)card.findViewById(R.id.departureAirportTextView)).setText(item.getDepartureAirport().getName());
                            ((TextView)card.findViewById(R.id.arrivalAirportTextView)).setText(item.getArrivalAirport().getName());
                            ((TextView)card.findViewById(R.id.departureTimeTextView)).setText(item.getDepartureTime());
                            ((TextView)card.findViewById(R.id.arrivalTimeTextView)).setText(item.getArrivalTime());
                            cardContainer.addView(card);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    }
}