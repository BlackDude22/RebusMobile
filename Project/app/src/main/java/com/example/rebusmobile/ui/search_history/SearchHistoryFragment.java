package com.example.rebusmobile.ui.search_history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rebusmobile.MainActivity;
import com.example.rebusmobile.R;
import com.example.rebusmobile.SearchItem;
import com.example.rebusmobile.UserSettings;

import java.util.ArrayList;

public class SearchHistoryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).loadHistory();
        loadSearchCards();
    }

    private void loadSearchCards(){
        ArrayList<SearchItem> searchItemList = UserSettings.getPreviousSearches();
        LinearLayout cardContainer = getView().findViewById(R.id.cardContainerSearchHistory);
        if(searchItemList.isEmpty())
            getActivity().findViewById(R.id.nothingFoundSearchHistoryTextView).setVisibility(View.VISIBLE);
        else getActivity().findViewById(R.id.nothingFoundSearchHistoryTextView).setVisibility(View.GONE);

        for (SearchItem item : searchItemList){
            View card = LayoutInflater.from(getActivity()).inflate(R.layout.search_card, null);
            ((TextView)card.findViewById(R.id.departureAirportTextView)).setText(item.getDepartureAirport());
            ((TextView)card.findViewById(R.id.arrivalAirportTextView)).setText(item.getArrivalAirport());
            ((TextView)card.findViewById(R.id.departureTimeTextView)).setText(item.getDepartureDate());
            ((TextView)card.findViewById(R.id.returnTimeTextView)).setText(item.getReturnDate());
            String passengersText = "";
            if (item.getNumberOfPassengers() > 1)
                passengersText = item.getNumberOfPassengers() + " passengers";
            else passengersText = "1 passenger";
            ((TextView)card.findViewById(R.id.passengersTextView)).setText(passengersText);
            String directText = "";
            if (item.allowOnlyDirectFlights())
                directText = "Direct flight";
            ((TextView)card.findViewById(R.id.directTextView)).setText(directText);
            final Bundle bundle = new Bundle();
            bundle.putString("DEPARTURE_AIRPORT", item.getDepartureAirport());
            bundle.putString("ARRIVAL_AIRPORT", item.getArrivalAirport());
            bundle.putString("DEPARTURE_DATE", item.getDepartureDate());
            bundle.putString("ARRIVAL_DATE", item.getReturnDate());
            bundle.putString("ONLY_DIRECT", Boolean.toString(item.allowOnlyDirectFlights()));
            bundle.putString("IS_ONE_WAY", Boolean.toString(item.isOneWay()));

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_search_result, bundle);
                }
            });
            cardContainer.addView(card);
        }
    }
}
