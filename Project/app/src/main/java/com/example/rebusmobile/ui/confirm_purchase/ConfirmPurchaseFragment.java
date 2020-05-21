package com.example.rebusmobile.ui.confirm_purchase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rebusmobile.Flight;
import com.example.rebusmobile.IResponseListener;
import com.example.rebusmobile.MainActivity;
import com.example.rebusmobile.R;
import com.example.rebusmobile.RebusNeoConnector;
import com.example.rebusmobile.UserSettings;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmPurchaseFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_purchase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText firstNameEditText = getActivity().findViewById(R.id.purchaseFirstNameEditText);
        final EditText lastNameEditText = getActivity().findViewById(R.id.purchaseLastNameEditText);
        final EditText phoneEditText = getActivity().findViewById(R.id.purchasePhoneNumberEditText);
        final EditText countryEditText = getActivity().findViewById(R.id.purchaseCountryEditText);
        final EditText cityEditText = getActivity().findViewById(R.id.purchaseCityEditText);
        final EditText streetEditText = getActivity().findViewById(R.id.purchaseStreetEditText);
        final EditText houseEditText = getActivity().findViewById(R.id.purchaseHouseEditText);

        final Button purchaseButton = getActivity().findViewById(R.id.confirmPurchaseButton);

        if (!UserSettings.getName().equals("null"))
            firstNameEditText.setText(UserSettings.getName());

        if (!UserSettings.getLastName().equals("null"))
            lastNameEditText.setText(UserSettings.getLastName());

        if (!UserSettings.getPhone().equals("null"))
            phoneEditText.setText(UserSettings.getPhone());

        if (!UserSettings.getCountry().equals("null"))
            countryEditText.setText(UserSettings.getCountry());

        if (!UserSettings.getCity().equals("null"))
            cityEditText.setText(UserSettings.getCity());

        if (!UserSettings.getStreet().equals("null"))
            streetEditText.setText(UserSettings.getStreet());

        if (!UserSettings.getHouse().equals("null"))
            houseEditText.setText(UserSettings.getHouse());

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCountry = countryEditText.getText().toString();
                String newCity = cityEditText.getText().toString();
                String newStreet = streetEditText.getText().toString();
                String newHouse = houseEditText.getText().toString();
                String newFirstName = firstNameEditText.getText().toString();
                String newLastName = lastNameEditText.getText().toString();
                String newPhone = phoneEditText.getText().toString();

                boolean hasErrors = false;

                if (newPhone.isEmpty()){
                    phoneEditText.setError(getString(R.string.change_phone_missing_error));
                    hasErrors = true;
                }

                if (newLastName.isEmpty()){
                    lastNameEditText.setError(getString(R.string.change_last_name_missing_error));
                    hasErrors = true;
                }

                if (newFirstName.isEmpty()){
                    firstNameEditText.setError(getString(R.string.change_first_name_missing_error));
                    hasErrors = true;
                }

                if (newCountry.isEmpty()){
                    countryEditText.setError(getString(R.string.change_address_country_missing_error));
                    hasErrors = true;
                }

                if (newCity.isEmpty()){
                    cityEditText.setError(getString(R.string.change_address_city_missing_error));
                    hasErrors = true;
                }

                if (newStreet.isEmpty()){
                    streetEditText.setError(getString(R.string.change_address_street_missing_error));
                    hasErrors = true;
                }

                if (newHouse.isEmpty()) {
                    houseEditText.setError(getString(R.string.change_address_house_number_missing_error));
                    hasErrors = true;
                }

                if (hasErrors)
                    return;

                final RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());
                String request = connector.getChangePersonalInfoRequest(UserSettings.getToken(), UserSettings.getId(), newFirstName, newLastName, newPhone, newCountry, newCity, newStreet, newHouse);
                connector.sendRequest(connector.POST, connector.REQUEST_PERSONAL_INFO, request, new IResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.v("TEST", response.toString());

                        try {
                            JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                            int errorCode = responseError.getInt("ErrorCode");
                            String errorMessage = responseError.getString("ErrorMessage");
                            UserSettings.updateToken((JSONObject) response);
                            if (errorCode == 0){
                                UserSettings.loadPersonalInfo((JSONObject) response);

                                String flightList = "";

                                if (UserSettings.getSelectedJourney().getForward() != null)
                                    for(Flight flight : UserSettings.getSelectedJourney().getForward().getRoute().getFlightList())
                                        if (!flightList.isEmpty())
                                            flightList += "," + flight.getID();
                                        else flightList += flight.getID();

                                if (UserSettings.getSelectedJourney().getBackward() != null)
                                    for(Flight flight : UserSettings.getSelectedJourney().getBackward().getRoute().getFlightList())
                                        if (!flightList.isEmpty())
                                            flightList += ", " + flight.getID();
                                        else flightList += flight.getID();

                                connector.sendRequest(connector.POST, connector.REQUEST_ORDER_JOURNEY, connector.getOrderJourneyRequest(UserSettings.getToken(), UserSettings.getId(), flightList), new IResponseListener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        try {
                                            JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                                            int errorCode = responseError.getInt("ErrorCode");
                                            String errorMessage = responseError.getString("ErrorMessage");
                                            UserSettings.updateToken((JSONObject) response);
                                            if (errorCode == 0){
                                                Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                                                connector.sendRequest(connector.GET, connector.REQUEST_BALANCE, connector.getBalanceGetRequest(UserSettings.getToken(), UserSettings.getId()), new IResponseListener() {
                                                    @Override
                                                    public void onResponse(Object response) {
                                                        UserSettings.loadBalance((JSONObject) response);
                                                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                                        navController.navigate(R.id.nav_home);
                                                    }

                                                    @Override
                                                    public void onError(String message) {
                                                        Log.v("Test", message);
                                                    }
                                                });
                                            } else if (errorCode == 999 || errorCode == 1) {
                                                UserSettings.clear();
                                                ((MainActivity)getActivity()).setLoggedOut();
                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                                navController.navigate(R.id.nav_home);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Log.v("TEEEEEEST order", message);
                                    }
                                });
                            }
                            else Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Log.v("TEST", message);
                    }
                });
            }
        });

    }
}
