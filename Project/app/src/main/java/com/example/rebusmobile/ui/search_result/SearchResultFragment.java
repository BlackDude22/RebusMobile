package com.example.rebusmobile.ui.search_result;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.rebusmobile.Flight;
import com.example.rebusmobile.IResponseListener;
import com.example.rebusmobile.Journey;
import com.example.rebusmobile.R;
import com.example.rebusmobile.RebusNeoConnector;
import com.example.rebusmobile.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class SearchResultFragment extends Fragment {

    private SearchResultViewModel searchResultViewModel;

    private String departureAirport;
    private String arrivalAirport;
    private String departureDate;
    private String arrivalDate;
    private String isOneWay;
    private String onlyDirect;
    private Integer numberOfPassengers;
    private int currentCardCount = 0;
    private int nextCardCount = 5;
    private Button showMoreButton;
    private Button sortByCheapestButton;
    private Button sortByMostExpensiveButton;
    private Button sortByShortestButton;

    ArrayList<Journey> journeys = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchResultViewModel =
                ViewModelProviders.of(this).get(SearchResultViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_result, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            departureAirport = bundle.getString("DEPARTURE_AIRPORT");
            arrivalAirport = bundle.getString("ARRIVAL_AIRPORT");
            departureDate = bundle.getString("DEPARTURE_DATE");
            arrivalDate = bundle.getString("ARRIVAL_DATE");
            isOneWay = bundle.getString("IS_ONE_WAY");
            onlyDirect = bundle.getString("ONLY_DIRECT");
        }
        RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());

        String request = connector.getJourneyRequest(departureAirport, arrivalAirport, departureDate, arrivalDate, isOneWay, onlyDirect);
        Log.v("TEST", request);

        connector.SendRequest(connector.GET, connector.REQUEST_FLIGHTS,request, new IResponseListener() {
            @Override
            public void onResponse(Object response) {
                loadJourneys((JSONObject) response);
                createJourneyCards();
            }
            @Override
            public void onError(String message) {
            }
        });

        showMoreButton = getView().findViewById(R.id.showMoreButton);
        sortByCheapestButton = getView().findViewById(R.id.sortByCheapest);
        sortByMostExpensiveButton = getView().findViewById(R.id.sortByMostExpensive);
        sortByShortestButton = getView().findViewById(R.id.sortByShortest);

        sortByCheapestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(journeys, new Comparator<Journey>() {
                    public int compare(Journey o1, Journey o2) {
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });
                currentCardCount = 0;
                nextCardCount = 5;
                clearCards();
                createJourneyCards();
            }
        });

        sortByMostExpensiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(journeys, new Comparator<Journey>() {
                    public int compare(Journey o1, Journey o2) {
                        return o1.getBest().compareTo(o2.getBest());
                    }
                });
                currentCardCount = 0;
                nextCardCount = 5;
                clearCards();
                createJourneyCards();
            }
        });

        sortByShortestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(journeys, new Comparator<Journey>() {
                    public int compare(Journey o1, Journey o2) {
                        return o1.getDuration().compareTo(o2.getDuration());
                    }
                });
                currentCardCount = 0;
                nextCardCount = 5;
                clearCards();
                createJourneyCards();
            }
        });

        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextCardCount += 5;
                createJourneyCards();
            }
        });

    }

    private void loadJourneys(JSONObject response)
    {

        ArrayList<Trip> toTrips = new ArrayList<>();
        ArrayList<Trip> fromTrips = new ArrayList<>();

        JSONArray trips = null;
        JSONArray toRoutesJSON = null;
        JSONObject toParamsJSON = null;
        JSONArray fromRoutesJSON = null;
        JSONObject fromParamsJSON = null;
        Calendar calendar = Calendar.getInstance();
        Log.v("TEST", "Loading JSONS " + calendar.getTimeInMillis());
        try {
            trips = response.getJSONObject("ResponseBody").getJSONArray("Entities").getJSONObject(0).getJSONArray("trips");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            toRoutesJSON = trips.getJSONObject(0).getJSONArray("routes");
            toParamsJSON = trips.getJSONObject(0).getJSONObject("tripParams");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            fromRoutesJSON = trips.getJSONObject(1).getJSONArray("routes");
            fromParamsJSON = trips.getJSONObject(1).getJSONObject("tripParams");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TEST", "Loading routes " + calendar.getTimeInMillis());
        if (toRoutesJSON != null){
            for (int i = 0; i < toRoutesJSON.length(); i++) {
                try {
                    toTrips.add(new Trip(toParamsJSON, toRoutesJSON.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (fromRoutesJSON != null) {
            for (int i = 0; i < fromRoutesJSON.length(); i++) {
                try {
                    fromTrips.add(new Trip(fromParamsJSON, fromRoutesJSON.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.v("TEST", "Loading trips " + calendar.getTimeInMillis());
        for (Trip forward: toTrips)
        {
            if (fromTrips.isEmpty()) {
                journeys.add(new Journey(forward, null));
            } else {
                for (Trip backward: fromTrips)
                    journeys.add(new Journey(forward, backward));
            }
        }

        Log.v("nx", "Journeys: " + journeys.size());
        Log.v("nx", "To Journeys: " + toTrips.size());
        Log.v("nx", "From Journeys: " + fromTrips.size());
    }

    private void createJourneyCards()
    {
        TextView nothingFound = getView().findViewById(R.id.nothingFoundTextView);
        if (journeys.size() == 0){
            nothingFound.setVisibility(View.VISIBLE);
            return;
        } else {
            nothingFound.setVisibility(View.GONE);
        }
        LinearLayout cardContainer = getView().findViewById(R.id.cardContainer);
        for(int i = currentCardCount; i < nextCardCount && i < journeys.size(); i++)
        {
            final Journey journey = journeys.get(i);

            CardView card = null;
            if (isOneWay.equals("false") && journey.getBackward() != null) {
                card = LayoutInflater.from(getContext()).inflate(R.layout.flight_card, null).findViewById(R.id.cardTemplate);
                ((ViewGroup) card.getParent()).removeView(card);
            } else {
                card = LayoutInflater.from(getContext()).inflate(R.layout.flight_card_half, null).findViewById(R.id.cardTemplate);
                ((ViewGroup) card.getParent()).removeView(card);
            }
            ((TextView)card.findViewById(R.id.cardToDepartureAirport)).setText(journey.getForward().getRoute().getRouteStart().getDepartureAirport().getName());
            ((TextView)card.findViewById(R.id.cardToArrivalAirport)).setText(journey.getForward().getRoute().getRouteEnd().getArrivalAirport().getName());
            ((TextView)card.findViewById(R.id.cardToDepartureTime)).setText(journey.getForward().getRoute().getRouteStart().getDepartureTime());
            ((TextView)card.findViewById(R.id.cardToArrivalTime)).setText(journey.getForward().getRoute().getRouteEnd().getArrivalTime());
            ((TextView)card.findViewById(R.id.cardToPrice)).setText(journey.getForward().getRoute().getPrice() + "€");
            int stopsTo = journey.getForward().getRoute().getStops();
            String stopsToText = "";
            if (stopsTo == 1)
                stopsToText = "Direct flight";
            else if (stopsTo == 2)
                stopsToText = String.valueOf(stopsTo - 1) + " stop";
            else stopsToText = String.valueOf(stopsTo - 1) + " stops";
            ((TextView)card.findViewById(R.id.cardToStops)).setText(stopsToText);


            if (isOneWay.equals("false") && journey.getBackward() != null) {
                ((TextView)card.findViewById(R.id.cardBackDepartureAirport)).setText(journey.getBackward().getRoute().getRouteStart().getDepartureAirport().getName());
                ((TextView)card.findViewById(R.id.cardBackArrivalAirport)).setText(journey.getBackward().getRoute().getRouteEnd().getArrivalAirport().getName());
                ((TextView)card.findViewById(R.id.cardBackDepartureTime)).setText(journey.getBackward().getRoute().getRouteStart().getDepartureTime());
                ((TextView)card.findViewById(R.id.cardBackArrivalTime)).setText(journey.getBackward().getRoute().getRouteEnd().getArrivalTime());
                ((TextView)card.findViewById(R.id.cardBackPrice)).setText(journey.getBackward().getRoute().getPrice() + "€");
                int stopsBack = journey.getBackward().getRoute().getStops();
                String stopsBackText = "";
                if (stopsBack == 1)
                    stopsBackText = "Direct flight";
                else if (stopsBack == 2)
                    stopsBackText = String.valueOf(stopsBack - 1) + " stop";
                else stopsBackText = String.valueOf(stopsBack - 1) + " stops";
                ((TextView)card.findViewById(R.id.cardBackStops)).setText(stopsBackText);
            }


            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View popupView = null;
                    if (isOneWay.equals("false") && journey.getBackward() != null) {
                        popupView = LayoutInflater.from(getActivity()).inflate(R.layout.flight_card_popup, null);
                    } else {
                        popupView = LayoutInflater.from(getActivity()).inflate(R.layout.flight_card_popup_half, null);
                    }

                    final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                    Button closePopup = popupView.findViewById(R.id.pupup_close_button);
                    closePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    ((TextView)popupView.findViewById(R.id.totalPriceDisplay)).setText(journey.getPrice().toString() + "€");
                    ((TextView)popupView.findViewById(R.id.totalOutboundDurationDisplay)).setText(journey.getForward().getRoute().getTotalTime());
                    ArrayList<Flight> flights = journey.getForward().getRoute().getFlightList();

                    LinearLayout outboundFlightContainer = (LinearLayout)popupView.findViewById(R.id.outboundFlights);

                    for (Flight flight : flights) {
                        TableLayout outboundFlight = LayoutInflater.from(getActivity()).inflate(R.layout.flight_info, null).findViewById(R.id.flightInfo);
                        ((TextView)outboundFlight.findViewById(R.id.departureAirport)).setText(flight.getDepartureAirport().getName());
                        ((TextView)outboundFlight.findViewById(R.id.departureTime)).setText(flight.getDepartureTime());
                        ((TextView)outboundFlight.findViewById(R.id.arrivalAirport)).setText(flight.getArrivalAirport().getName());
                        ((TextView)outboundFlight.findViewById(R.id.arrivalTime)).setText(flight.getArrivalTime());
                        outboundFlightContainer.addView(outboundFlight);
                    }

                    if (isOneWay.equals("false") && journey.getBackward() != null) {
                        ((TextView)popupView.findViewById(R.id.totalInboundDurationDisplay)).setText(journey.getBackward().getRoute().getTotalTime());
                        ArrayList<Flight> inboundFlights = journey.getBackward().getRoute().getFlightList();

                        LinearLayout inboundFlightContainer = (LinearLayout)popupView.findViewById(R.id.inboundFlights);

                        for (Flight flight : inboundFlights) {
                            TableLayout inboundFlight = LayoutInflater.from(getActivity()).inflate(R.layout.flight_info, null).findViewById(R.id.flightInfo);
                            ((TextView)inboundFlight.findViewById(R.id.departureAirport)).setText(flight.getDepartureAirport().getName());
                            ((TextView)inboundFlight.findViewById(R.id.departureTime)).setText(flight.getDepartureTime());
                            ((TextView)inboundFlight.findViewById(R.id.arrivalAirport)).setText(flight.getArrivalAirport().getName());
                            ((TextView)inboundFlight.findViewById(R.id.arrivalTime)).setText(flight.getArrivalTime());
                            inboundFlightContainer.addView(inboundFlight);
                        }

                    }

                    popupWindow.showAsDropDown(popupView, 0, 0);
                }
            });
            cardContainer.addView(card);
            currentCardCount++;
        }

        if (currentCardCount < journeys.size())
            showMoreButton.setVisibility(View.VISIBLE);
        else showMoreButton.setVisibility(View.GONE);
    }

    private void clearCards(){
        LinearLayout cardContainer = getView().findViewById(R.id.cardContainer);
        ((ViewGroup)cardContainer).removeAllViewsInLayout();
    }
}