package com.example.rebusmobile.ui.change_address;

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

import com.example.rebusmobile.IResponseListener;
import com.example.rebusmobile.R;
import com.example.rebusmobile.RebusNeoConnector;
import com.example.rebusmobile.UserSettings;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeAddressFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText countryEditText = getActivity().findViewById(R.id.newCountryEditText);
        final EditText cityEditText = getActivity().findViewById(R.id.newCityEditText);
        final EditText streetEditText = getActivity().findViewById(R.id.newStreetEditText);
        final EditText houseEditText = getActivity().findViewById(R.id.newHouseEditText);
        final Button button = getActivity().findViewById(R.id.changeAddressButton);

        if (!UserSettings.getCountry().equals("null"))
            countryEditText.setText(UserSettings.getCountry());

        if (!UserSettings.getCity().equals("null"))
            cityEditText.setText(UserSettings.getCity());

        if (!UserSettings.getStreet().equals("null"))
            streetEditText.setText(UserSettings.getStreet());

        if (!UserSettings.getHouse().equals("null"))
            houseEditText.setText(UserSettings.getHouse());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCountry = countryEditText.getText().toString();
                String newCity = cityEditText.getText().toString();
                String newStreet = streetEditText.getText().toString();
                String newHouse = houseEditText.getText().toString();

                boolean hasErrors = false;

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

                RebusNeoConnector connector = RebusNeoConnector.getInstance(getContext());
                connector.sendRequest(connector.POST, connector.REQUEST_PERSONAL_INFO, connector.getChangeAddressRequest(UserSettings.getToken(), UserSettings.getId(), newCountry, newCity, newStreet, newHouse), new IResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.v("TEST", response.toString());

                        try {
                            JSONObject responseError = ((JSONObject)response).getJSONObject("Header").getJSONObject("ResponseError");
                            int errorCode = responseError.getInt("ErrorCode");
                            String errorMessage = responseError.getString("ErrorMessage");
                            if (errorCode == 0){
                                UserSettings.loadPersonalInfo((JSONObject) response);
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                navController.navigate(R.id.nav_settings_logged_in);
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
